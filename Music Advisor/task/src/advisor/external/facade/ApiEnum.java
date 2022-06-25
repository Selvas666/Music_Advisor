package advisor.external.facade;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ApiEnum {
    CATEGORIES (),
    FEATURED(),
    NEW(),
    PLAYLISTS();
}
