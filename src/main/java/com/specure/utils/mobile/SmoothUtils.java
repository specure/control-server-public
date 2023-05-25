package com.specure.utils.mobile;

import com.specure.dto.mobile.MobileGraph;
import com.specure.service.mobile.Smoothable;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class SmoothUtils {
    private final static int SMOOTHING_DATA_AMOUNT = 5;

    public List<MobileGraph> smooth(final List<MobileGraph> valueList) {
        if (valueList == null || valueList.size() < SMOOTHING_DATA_AMOUNT) {
            return valueList;
        }

        final int startingIndex = getStartingIndex(valueList, SMOOTHING_DATA_AMOUNT);
        final int endingIndex = getEndingIndex(valueList, SMOOTHING_DATA_AMOUNT);

        if (startingIndex > endingIndex) {
            return valueList;
        }

        final List<MobileGraph> resultList = new ArrayList<>();
        for (int i = startingIndex; i <= endingIndex; i++) {
            resultList.add(
                    MobileGraph.builder()
                            .bytesTotal(smoothYPoint(valueList, i, SMOOTHING_DATA_AMOUNT))
                            .timeElapsed(smoothXPoint(valueList, i, SMOOTHING_DATA_AMOUNT))
                            .build()
            );
        }

        return resultList;
    }

    private double smoothYPoint(final List<? extends Smoothable> valueList, final int index, final int dataAmount) {
        final int i = Math.max(index - (dataAmount / 2), 0);
        final int j = Math.min(index + (dataAmount / 2), valueList.size());

        double sum = 0;
        for (int x = i; x <= j; x++) {
            sum += valueList.get(x).getYValue();
        }

        return sum / (double) (j - i + 1);
    }

    private double smoothXPoint(List<? extends Smoothable> valueList, int index, int dataAmount) {
        final int i = Math.max(index - (dataAmount / 2), 0);
        final int j = Math.min(index + (dataAmount / 2), valueList.size());

        double sum = 0;
        for (int x = i; x <= j; x++) {
            sum += valueList.get(x).getXValue();
        }

        return sum / (double) (j - i + 1);
    }

    public int getStartingIndex(List<? extends Smoothable> valueList, int dataAmount) {
        return Math.min(dataAmount / 2, valueList.size());
    }

    public int getEndingIndex(List<? extends Smoothable> valueList, int dataAmount) {
        return Math.max(valueList.size() - dataAmount / 2 - 1, 0);
    }
}
