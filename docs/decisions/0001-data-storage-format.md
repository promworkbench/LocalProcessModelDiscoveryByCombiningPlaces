---
Status: Draft  
Date: 2026-04-17  
Authors: VikiPeeva 
Reviewers: 
Replaces:  
Superseded by:
---

# ADR-0001: LPM Set - Log Alignment Data Storage Format

## Context

We need to store _Local Process Models (LPMs) and their occurrence lists in an event log_. Event log sizes can vary, 
but from the standard ones used for research in process mining, they go up to 1.GB. LPMs are usually small models 
covering on average up to five activities, but for a single event log tens of thousands can be discovered. 
Moreover, LPMs can come with additional attributes about them, either computed from the occurrence lists or maybe 
manual annotations.

If one wants to be future-proof, a pessimistic estimation for size could be event logs processed by Celonis 
(from a couple of hundreds GB to TB of data).

#### Input

- Event log `log.xes` (XES format)
- LPM set (ZIP of PNML files)

For now, we make an assumption that the event log is an XES file and the set of LPMs is in a zip, where each individual
LPM is in a PNML format (the format can also be any of the other process model formats: BPMN, process tree, etc.).

The directory tree of the input files is as follows:

```ascii
├── lpms.zip
│   ├── lpm_1.pnml
│   ├── lpm_2.pnml
│   ├── ...
│   ├── lpm_n.pnml
└── log.xes
```

## Decision Drivers

The choice affects:
- portability,
- storage efficiency,
- long-term maintenance cost,
- operational complexity,
- query performance (future),
- streaming (future).

Expected access patterns for the data currently are:
- Complete read/write
- Streamed read/write
- Random access read/write
- Query-based read/write

There also exist various ways one can align LPMs to an event log:

- The occurrence must occur within a locality window
- Aligned events must be adjacent
- One event can be used in multiple alignments
- etc.

Taking these variations into consideration, we have two options regarding the storage:
- The storage focuses on one variation
- The storage includes occurrence lists for multiple (or all possible) variations

## Options Considered

### Option A: ZIP of models and XES

The occurrence list is stored directly in the event log such that for each event in the XES there is an attribute
`covering-lpms` where all LPM ids of the LPMs covering the event are listed. We use the file name of each LPM as the id to denote it in the list.

#### Output

```ascii
result/
├── lpms/
│   ├── lpm_1.pnml
│   ├── lpm_2.pnml
│   ├── ...
│   ├── lpm_n.pnml
└── aligned_log.xes
```

The aligned event log `aligned_log.xes` is stored in the traditional XES format, but each event has an additional
attribute `covering-lpms` that lists all LPMs covering the event.

For each lpm, the concrete occurrence lists need to be reconstructed from the covered events.

#### Example:

**Input:**

$L = \langle a, b, a, c, d\rangle, \langle a, x, d\rangle$

$lpm1$: a -> d

$lpm2$: a -> b -> d

**Output:** The aligned event log is stored in the traditional XES format, but each event has an additional attribute
`covering-lpms` that lists all LPMs covering the event.

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<log xes.version="1.0" xes.features="nested-attributes" openxes.version="1.0RC7">
	<trace>
		<event>
			<string key="concept:name" value="a"/>
			<list key="covering-lpms" value="[lpm1, lpm2]"/>
		</event>
		<event>
			<string key="concept:name" value="b"/>
			<list key="covering-lpms" value="[lpm2]"/>
		</event>
        <event>
            <string key="concept:name" value="a"/>
            <list key="covering-lpms" value="[lpm1]"/>
        </event>
		<event>
			<string key="concept:name" value="c"/>
			<list key="covering-lpms" value="[]"/>
		</event>
		<event>
			<string key="concept:name" value="d"/>
			<list key="covering-lpms" value="[lpm1, lpm2]"/>
		</event>
	</trace>
	<trace>
		<event>
			<string key="concept:name" value="a"/>
			<list key="covering-lpms" value="[lpm1]"/>
		</event>
		<event>
			<string key="concept:name" value="d"/>
			<list key="covering-lpms" value="[lpm1]"/>
		</event>
	</trace>
</log>

```


**Pros**
- Using only existing formats

**Cons**

- Complete occurrence lists need to be reconstructed from the language of the model.
  > Consider the sequence model $a \rightarrow b$ and the log $L = [\langle e1:a, e2:a, e3:b \rangle]$. Each event
  > will be covered by the model, but if one wants to reconstruct the occurrence list, one should get $[\langle
  > e1:a, e3:b \rangle, \langle e2:a, e3:b \rangle]$ and cannot simply just take all events covered by the model.
  > The only way to do this would be to do a lookup at the model's language or do a replay on the model, both of which
  > are not cheap operations.
- Additional attributes for the models cannot be stored
- Misusing the xes standard

**Alternatives**

To be able to store additional attributes, a new format for the models can be introduced or additional json file 
can be used only for storing the additional attributes.

### Option B: Separate Full Alignments File in Human-Readable Format (e.g., JSON)

For each set of models for which an occurrence list is computed, a report is generated that includes:

- lpm set info (lpm set title, lpm set filename, lpm set count, etc.),
- event log info,
- aligned traces per LPM

#### Output

```ascii
result/
├── lpms/
│   ├── lpm_1.pnml
│   ├── lpm_2.pnml
│   ├── ...
│   ├── lpm_n.pnml
├── log.xes
└── alignments.json
```

The alignment between the event log and the lpms is stored in a `json` file that contains an alignment between each pair
of an lpm and trace. For each event, it is stored whether a synchronous move (t_x) or a move on log (<<) was performed.
The event ids are used to denote the events in `log.xes` and the lpm ids are from the `lpms/` directory.

For each lpm, the concrete occurrence lists need to be reconstructed from the stored alignments.
#### Example:

**Input:**

$L = \langle e1:a, e2:b, e3:a, e4:c, e5:d\rangle, \langle e6:a, e7:x, e8:d\rangle$ (file1)

$lpm1$: a -> d (file2)

$lpm2$: a -> b -> d (file3) (or all lpms in one file)

**Output:**

Alignments in proposed file format:

```json
 {
  "meta": {
    "eventlog": "name-id",
    "lpm_set": "name-id",
    "variant": ["gapped-2", "local-4"]
  },
 "alignments": {
   "lpm1": {
     "t1": {
       "e1": "t_a", 
       "e2": ">>",
       "e3": "t_a",
       "e4": ">>", 
       "e5": "t_d"
     }, 
     "t2": {
       "e1": "t_a", 
       "e2": ">>", 
       "e3": "t_d"
     }
   }, 
   "lpm2": {
     "t1": {
       "e1": "t_a", 
       "e2": "t_b",
       "e3": ">>",
       "e4": ">>",
       "e5": "t_d"
     }, 
     "t2": {
       "e1": ">>",
       "e2": ">>",
       "e3": ">>"
     }
   }
 }
}
```

The reconstructed occurrence lists would be:

- For $lpm1$: $\{\langle e1:a, e5:d \rangle,\langle e3:a, e5:d \rangle, \langle e6:a, e8:d \rangle\}$
- For $lpm2$: $\{\langle e1:a, e2:b, e5:d \rangle\}$

**Pros**
- Alignments are directly available

**Cons**

- Problematic when one event is in two alignments of the same LPM. Reconstruction needs to be done.
- The entire event log is duplicated for each LPM

**Alternatives**

- Same as with Option A, where each event occurs once, but in a separate alignment file in JSON format.
- Same as this option, but if each event has a unique id, there will be no need for the traces in the hierarchy.

### Option C: Separate Occurrence List File in Human-Readable Format (e.g., JSON)

For each set of models for which an occurrence list is computed, a report is generated that includes:
- lpm set info,
- event log info,
- occurrence list

#### Output

```ascii
result/
├── lpms/
│   ├── lpm_1.pnml
│   ├── lpm_2.pnml
│   ├── ...
│   ├── lpm_n.pnml
├── log.xes
└── alignments.json
```

The alignment between the event log and the lpms is stored in a `json` file that contains all optimal alignments between
each pair of an lpm and trace. The event ids are used to denote the events in `log.xes` and the lpm ids are from the
`lpms/` directory.

For each lpm, the concrete occurrence are directly available.
#### Example:

**Input:**

$L = \langle a, b, a, c, d\rangle, \langle a, x, d\rangle$

$lpm1$: a -> d

$lpm2$: a -> b -> d

**Output:**
```json
{ 
 "meta": {
	 "eventlog": "name-id",
	 "lpm_set": "name-id",
	 "variant": ["gapped-2", "local-4"]
 },
 "alignments": {
   "lpm1": {
     "alignment1": ["t1-e1", "t1-e5"],
     "alignment2": ["t1-e3", "t1-e5"],
     "alignment3": ["t2-e1", "t2-e3"]
   },
   "lpm2": {
     "alignment1": ["t1-e1", "t1-e2", "t1-e5"]
   }
 }
}
```

**Pros**
- Occurrence lists are directly available
- Additional LPM attributes can be stored

**Cons**
- Events are multiplied for each alignment and LPM pair

## Decision

<!-- To be filled in once review is complete. -->

## Consequences

<!-- To be filled in once review is complete. -->

## References

- 
