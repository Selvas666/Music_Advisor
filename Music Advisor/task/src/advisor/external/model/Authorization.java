package advisor.external.model;

import io.vavr.collection.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Authorization {
    List <String> communicate;
    boolean result;
    String token;
}
