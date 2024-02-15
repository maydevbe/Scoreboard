package io.github.maydevbe.layout;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ScoreboardLayoutManager {

    private String title;

    private List<String> lines = new ArrayList<>();

    public void addLine(String text) {
        lines.add(text);
    }

    public void addLine(int index, String text) {
        lines.add(index, text);
    }
}
