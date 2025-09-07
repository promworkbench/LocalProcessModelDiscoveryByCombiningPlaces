package org.processmining.placebasedlpmdiscovery.runners.lpmutils;

import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.replayer.ReplayableLocalProcessModelReplayer;

import java.util.List;
import java.util.Set;

public class LPMLanguageGeneratorRunner {

    public static void main(String[] args) {
//        System.out.println("LPM 1");
//        printPaths(LocalProcessModel.from("(W_Wijzigen_contractgegevens | " +
//                "O_SELECTED)(W_Wijzigen_contractgegevens | O_SENT)"));
//
        System.out.println("LPM 2");
        printPaths(LocalProcessModel.from("(A_ACCEPTED | A_CANCELLED)(A_PARTLYSUBMITTED | A_ACCEPTED)"));

        System.out.println("LPM 3");
        printPaths(LocalProcessModel.from("(A_ACCEPTED | A_CANCELLED)(A_PARTLYSUBMITTED | A_ACCEPTED)(A_PREACCEPTED | A_ACCEPTED)"));
//
//        System.out.println("LPM 4");
//        printPaths(LocalProcessModel.from("(Viki Acko | Food)"));
//
//        System.out.println("LPM 5");
//        printPaths(LocalProcessModel.from("(Viki Acko | Food)(Acko Matea | Drinks)"));

//        System.out.println("LPM 6");
//        printPaths(LocalProcessModel.from("(Acko | Walk)(Viki | Walk TV)(Matea | TV)"));
    }

    private static void printPaths(LocalProcessModel lpm) {
        Set<List<String>> paths = ReplayableLocalProcessModelReplayer.findAllPaths(10, lpm);
        for (List<String> path : paths) {
            System.out.println(path);
        }
    }
}
