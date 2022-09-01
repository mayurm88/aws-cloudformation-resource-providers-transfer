package com.stedi.transfer.agreement;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Converter {

    static class TagConverter {
        static software.amazon.awssdk.services.transfer.model.Tag toSdk(
                com.stedi.transfer.agreement.Tag tag) {
            if (tag == null) {
                return null;
            }
            return software.amazon.awssdk.services.transfer.model.Tag.builder()
                    .key(tag.getKey())
                    .value(tag.getValue())
                    .build();
        }

        static Set<com.stedi.transfer.agreement.Tag> translateTagfromMap(Map<String, String> tags) {
            if (tags == null) {
                return Collections.emptySet();
            }

            return tags.entrySet()
                    .stream()
                    .map(tag -> com.stedi.transfer.agreement.Tag.builder()
                            .key(tag.getKey())
                            .value(tag.getValue())
                            .build())
                    .collect(Collectors.toSet());
        }

        static com.stedi.transfer.agreement.Tag fromSdk(software.amazon.awssdk.services.transfer.model.Tag tag) {
            if (tag == null) {
                return null;
            }
            return com.stedi.transfer.agreement.Tag.builder()
                    .key(tag.key())
                    .value(tag.value())
                    .build();
        }
    }
}
