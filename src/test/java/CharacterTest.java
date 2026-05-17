import org.example.Character;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CharacterTest {

    @Test
    void 문자열로_생성한다() {
        // given
        String name = "example";

        // when
        Character character = new Character(name);

        // then
        assertEquals(name, character.getName());
    }
}
