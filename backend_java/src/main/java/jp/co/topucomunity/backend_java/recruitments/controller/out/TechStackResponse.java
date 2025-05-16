package jp.co.topucomunity.backend_java.recruitments.controller.out;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor(staticName = "from")
@Getter
public class TechStackResponse {
    private List<String> techStacks = new ArrayList<>();
}
