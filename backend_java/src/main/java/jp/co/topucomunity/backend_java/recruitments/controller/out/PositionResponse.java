package jp.co.topucomunity.backend_java.recruitments.controller.out;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor(staticName = "from")
public class PositionResponse {

    private List<String> positions = new ArrayList<>();

}
