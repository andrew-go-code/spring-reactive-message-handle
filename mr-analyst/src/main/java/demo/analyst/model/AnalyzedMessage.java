package demo.analyst.model;

import lombok.Value;

@Value
public class AnalyzedMessage {
    String text;
    Integer status;
}
