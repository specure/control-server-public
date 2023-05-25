package com.specure.mapper.mobile.impl;

import com.specure.common.enums.Direction;
import com.specure.common.model.elastic.SimpleSpeedDetail;
import com.specure.dto.mobile.MobileGraph;
import com.specure.dto.mobile.SpeedCurve;
import com.specure.mapper.mobile.MobileGraphMapper;
import com.specure.response.mobile.MobileGraphResponse;
import com.specure.utils.mobile.SmoothUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MobileGraphMapperImpl implements MobileGraphMapper {

    @Override
    public MobileGraphResponse basicTestToGraphResponse(List<SimpleSpeedDetail> speedDetails) {
        if (CollectionUtils.isEmpty(speedDetails)) {
            return emptyMobileGraphResponse();
        }

        List<MobileGraph> downloadSpeeds = getSmoothedSpeedItemsByDirection(speedDetails, Direction.download);
        List<MobileGraph> uploadSpeeds = getSmoothedSpeedItemsByDirection(speedDetails, Direction.upload);

        return MobileGraphResponse.builder()
                .speedCurve(new SpeedCurve(downloadSpeeds, uploadSpeeds))
                .build();
    }

    private MobileGraphResponse emptyMobileGraphResponse() {
        return MobileGraphResponse.builder()
                .speedCurve(
                        SpeedCurve.builder()
                                .download(Collections.emptyList())
                                .upload(Collections.emptyList())
                                .build())
                .build();
    }

    private List<MobileGraph> getSmoothedSpeedItemsByDirection(List<SimpleSpeedDetail> speedDetail, Direction upload) {
        return getSmoothedSpeedItems(
                speedDetail.stream()
                        .filter(x -> upload.equals(x.getDirection()))
                        .collect(Collectors.toList())
        );
    }

    private List<MobileGraph> getSmoothedSpeedItems(List<SimpleSpeedDetail> speedDetail) {
        Map<Integer, List<SimpleSpeedDetail>> items = speedDetail.stream().collect(Collectors.groupingBy(SimpleSpeedDetail::getThread));
        sortItems(items);
        if (items == null) {
            return new ArrayList<>();
        }

        int numItems = 0;
        for (List<SimpleSpeedDetail> speedItems : items.values())
            numItems += speedItems.size();

        final long[] times = new long[numItems];

        int i = 0;
        for (List<SimpleSpeedDetail> speedItems : items.values()) {
            for (SimpleSpeedDetail item : speedItems)
                times[i++] = item.getTime();
            if (i == times.length)
                break;
        }

        numItems = i;
        Arrays.sort(times);

        final long[] bytes = new long[times.length];
        for (Map.Entry<Integer, List<SimpleSpeedDetail>> entry : items.entrySet()) {
            i = 0;
            long lastTime = 0;
            long lastBytes = 0;
            for (SimpleSpeedDetail si : entry.getValue()) {
                while (si.getTime() > times[i]) // average times we don't have
                {
                    bytes[i] += Math.round((double) ((times[i] - lastTime) * si.getBytes() + (si.getTime() - times[i]) * lastBytes) / (si.getTime() - lastTime));
                    i++;
                }
                if (si.getTime() == times[i])
                    bytes[i++] += si.getBytes();
                lastTime = si.getTime();
                lastBytes = si.getBytes();
            }
            while (i < numItems)
                bytes[i++] += lastBytes; // assume no transfer after last entry; might not be the case, but assuming otherwise could be worse
        }

        final List<MobileGraph> result = new ArrayList<>();
        for (int j = 0; j < numItems; j++)
            result.add(new MobileGraph(bytes[j], times[j]));

        return SmoothUtils.smooth(result);
    }

    private void sortItems(Map<Integer, List<SimpleSpeedDetail>> items) {
        if (items == null)
            return;
        for (List<SimpleSpeedDetail> list : items.values())
            list.sort(Comparator.comparingLong(SimpleSpeedDetail::getTime));
    }
}
