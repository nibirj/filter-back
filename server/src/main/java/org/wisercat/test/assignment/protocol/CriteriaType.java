package org.wisercat.test.assignment.protocol;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CriteriaType {
    Amount("Amount"),
    Title("Title"),
    Date("Date");
    private final String value;
}
