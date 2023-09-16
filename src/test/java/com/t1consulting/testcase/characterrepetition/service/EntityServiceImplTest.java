package com.t1consulting.testcase.characterrepetition.service;


import com.t1consulting.testcase.characterrepetition.dto.request.CharacterEntityRequestDto;
import com.t1consulting.testcase.characterrepetition.dto.response.RepetitionsEntityResponseDto;
import com.t1consulting.testcase.characterrepetition.exceptions.NotFoundException;
import com.t1consulting.testcase.characterrepetition.model.CharacterEntity;
import lombok.RequiredArgsConstructor;
import org.assertj.core.internal.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintViolationException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EntityServiceImplTest {
    private final EntityService service;

    @Test
    @Order(1)
    public void addEntity_correctValue_shouldReturnEntityDto() {
        CharacterEntityRequestDto request = CharacterEntityRequestDto.builder()
                .text("example")
                .build();
        CharacterEntity expected = CharacterEntity.builder()
                .id(1L)
                .text("example")
                .build();
        var result = service.add(request);
        assertEquals(result.getId(), expected.getId());
        assertEquals(result.getText(), expected.getText());
    }

    @Test
    public void addEntity_incorrectTextLength_shouldReturnException() {
        String text = RandomString.make(5001);
        var request = CharacterEntityRequestDto.builder()
                .text(text)
                .build();

        assertThrows(ConstraintViolationException.class, () -> service.add(request));

        String text2 = "";
        var request2 = CharacterEntityRequestDto.builder()
                .text(text2)
                .build();
        assertThrows(ConstraintViolationException.class, () -> service.add(request2));
    }

    @Test
    public void addEntity_alreadyExists_shouldReturnEntityDto() {
        CharacterEntityRequestDto request = CharacterEntityRequestDto.builder()
                .text("exists")
                .build();
        var expected = service.add(request);
        var result = service.add(request);
        assertEquals(result.getId(), expected.getId());
        assertEquals(result.getText(), expected.getText());
    }

    @Test
    public void getRepetitionsById_shouldReturnCorrectValue() {
        CharacterEntityRequestDto request = CharacterEntityRequestDto.builder()
                .text("eeests")
                .build();
        var entity = service.add(request);
        var result = service.getRepetitionsById(entity.getId());
        Map<Character, Integer> expectedMap = new LinkedHashMap<>();
        expectedMap.put('e', 3);
        expectedMap.put('s', 2);
        expectedMap.put('t', 1);
        String expectedText = "\"e\":3,\"s\":2,\"t\":1";

        assertEquals(result.getRepetitions(), expectedMap);
        assertEquals(result.getResult(), expectedText);
    }

    @Test
    public void getRepetitionsById_nonExistsEntity_shouldReturnNotFoundException() {
        assertThrows(NotFoundException.class, () -> service.getRepetitionsById(-1L));
    }

    @Test
    public void getRepetitions_correctValues_shouldReturnExpectedValue() {
        String expectedText = "\"u\":3,\"e\":2,\"a\":1";
        String testText = "ueauue";
        var result = service.getRepetitions(testText);
        assertEquals(result.getResult(), expectedText);

        var result2 = service.getRepetitions(CharacterEntityRequestDto.builder()
                .text(testText)
                .build());
        assertEquals(result2.getResult(), expectedText);
    }

    @Test
    public void findRepetition_differentTests_shouldReturnCorrect() {
        String text1 = "aAbBaabAAA";
        String expected1 = "\"A\":4,\"a\":3,\"b\":2,\"B\":1";

        String text2 = "aaabcb";
        String expected2 = "\"a\":3,\"b\":2,\"c\":1";

        String text3 = "12jн1н2н22";
        String expected3 = "\"2\":4,\"н\":3,\"1\":2,\"j\":1";

        try {
            Method method = service.getClass().getDeclaredMethod("findRepetitions", String.class);
            method.setAccessible(true);
            RepetitionsEntityResponseDto result1 = (RepetitionsEntityResponseDto) method.invoke(service, text1);
            RepetitionsEntityResponseDto result2 = (RepetitionsEntityResponseDto) method.invoke(service, text2);
            RepetitionsEntityResponseDto result3 = (RepetitionsEntityResponseDto) method.invoke(service, text3);
            assertEquals(expected1, result1.getResult());
            assertEquals(expected2, result2.getResult());
            assertEquals(expected3, result3.getResult());
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }

    }

}
