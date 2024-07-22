package org.processmining.placebasedlpmdiscovery.view;

import javax.swing.*;

public class OldJavaUtils {

    public static int[] getSelectedIndices(ListSelectionModel lsm) {
        int iMin = lsm.getMinSelectionIndex();
        int iMax = lsm.getMaxSelectionIndex();

        if ((iMin < 0) || (iMax < 0)) {
            return new int[0];
        }

        int[] rvTmp = new int[1+ (iMax - iMin)];
        int n = 0;
        for(int i = iMin; i <= iMax; i++) {
            if (lsm.isSelectedIndex(i)) {
                rvTmp[n++] = i;
            }
        }
        int[] rv = new int[n];
        System.arraycopy(rvTmp, 0, rv, 0, n);
        return rv;
    }
}
