package com.stedi.transfer.profile;

import software.amazon.awssdk.services.transfer.model.Tag;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Converter {
    static class TagConverter {
        static Tag toSdk(
                com.stedi.transfer.profile.Tag tag) {
            if (tag == null) {
                return null;
            }
            return Tag.builder()
                    .key(tag.getKey())
                    .value(tag.getValue())
                    .build();
        }

        static Set<com.stedi.transfer.profile.Tag> translateTagfromMap(Map<String, String> tags) {
            if (tags == null) {
                return Collections.emptySet();
            }

            return tags.entrySet()
                    .stream()
                    .map(tag -> com.stedi.transfer.profile.Tag.builder()
                            .key(tag.getKey())
                            .value(tag.getValue())
                            .build())
                    .collect(Collectors.toSet());
        }

        static com.stedi.transfer.profile.Tag fromSdk(Tag tag) {
            if (tag == null) {
                return null;
            }
            return com.stedi.transfer.profile.Tag.builder()
                    .key(tag.key())
                    .value(tag.value())
                    .build();
        }
    }
}
