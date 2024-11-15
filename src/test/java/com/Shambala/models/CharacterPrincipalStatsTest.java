package com.Shambala.models;

import com.Shambala.Enum.StatType;
import com.Shambala.models.builder.CharacterPrincipalStatBuilder;
import com.Shambala.models.builder.CharacterSubStatsBuilder;
import com.Shambala.models.export.CharacterPrincipalStatExport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.dao.DuplicateKeyException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CharacterPrincipalStatsTest {

    private StatType statType;
    private int value;
    private List<CharacterSubStats> subStatsList;
    private Character character;

    @BeforeEach
    void setUp() {
        statType = StatType.PSYCHIC;
        value = 45;
        subStatsList = new ArrayList<>();
        character = new Character();

    }

    private record TestBuilder(StatType getStatType, int getValue, List<CharacterSubStats> getSubStatsList, Character getCharacter) implements CharacterPrincipalStatBuilder {

    }

    private CharacterPrincipalStatBuilder createTestPrincipalStat() {
        return new TestBuilder(statType, value, subStatsList, character);
    }

    @Test
    void should_create_character_principal_stat_from_builder() {
        CharacterPrincipalStat characterPrincipalStat = CharacterPrincipalStat.fromBuilder(new TestBuilder(StatType.PSYCHIC, 50, subStatsList, character));
        assertNotNull(characterPrincipalStat);
    }

    @Test
    void testCreatePrincipalStats_whenStatTypeIsProvided_returnStatTypeValue() {
        CharacterPrincipalStat principalStatTest = CharacterPrincipalStat.fromBuilder(createTestPrincipalStat());
        assertEquals(statType, principalStatTest.getStatType());
    }

    @Test
    void testCreatePrincipalStats_whenValueIsProvided_returnValue() {
        CharacterPrincipalStat principalStatTest = CharacterPrincipalStat.fromBuilder(createTestPrincipalStat());
        assertEquals(value, principalStatTest.getValue());
    }

    @ParameterizedTest
    @ValueSource(ints = {5, 60, 47})
    void testCreatePrincipalStat_whenValueIsProvided_shouldBeGreaterThan10LesserThan50AndDivisibleBy5(int value) {
        this.value = value;
        IllegalArgumentException statValueException = assertThrows(IllegalArgumentException.class,
                () -> CharacterPrincipalStat.fromBuilder(createTestPrincipalStat()));

        assertEquals("Principal stat value should be greater than 10, lesser than 50 and divisible by 5", statValueException.getMessage());
    }

    @Test
    void testCreatePrincipalStat_whenPrincipalStatAreProvided_returnListOfSubStat() {
        CharacterPrincipalStat principalStatTest = CharacterPrincipalStat.fromBuilder(createTestPrincipalStat());
        assertEquals(subStatsList, principalStatTest.getSubStatsList());
    }

    @Test
    void testCreatePrincipalStat_whenAddNewSubStat_subStatListShouldContainFourSubStatMax()  {
        CharacterPrincipalStat principalStatTest = CharacterPrincipalStat.fromBuilder(createTestPrincipalStat());
        CharacterSubStats mockSubStat = mock(CharacterSubStats.class);

        for (int i = 0; i < 16; i++) {
            CharacterSubStatsBuilder mockSubStatBuilder = mock(CharacterSubStatsBuilder.class);
            when(mockSubStatBuilder.getStatType()).thenReturn(StatType.PSYCHIC);
            when(mockSubStatBuilder.getSubStatName()).thenReturn("Erudition");
            when(mockSubStatBuilder.getSubStatValue()).thenReturn(30);
            when(mockSubStatBuilder.getDescription()).thenReturn("Hello world");

            CharacterSubStats subStats = CharacterSubStats.fromSubStatBuilder(mockSubStatBuilder);
            subStatsList.add(subStats);
        }
        ArrayIndexOutOfBoundsException sizeSubStatListException = assertThrows(ArrayIndexOutOfBoundsException.class,
                () -> principalStatTest.addNewSubStatWithNoDuplicationNameAndSizeLimit(mockSubStat));

        assertEquals("SubStat list should contain 20 sub stat", sizeSubStatListException.getMessage());
    }

    @Test
    void testCreatePrincipalStat_whenAddNewSubStat_newSubStatCanNotHaveASameName() {
        CharacterPrincipalStat principalStatTest = CharacterPrincipalStat.fromBuilder(createTestPrincipalStat());

        CharacterSubStatsBuilder mockSubStatBuilder = mock(CharacterSubStatsBuilder.class);
        when(mockSubStatBuilder.getStatType()).thenReturn(StatType.PSYCHIC);
        when(mockSubStatBuilder.getSubStatName()).thenReturn("Erudition");
        when(mockSubStatBuilder.getSubStatValue()).thenReturn(30);
        when(mockSubStatBuilder.getDescription()).thenReturn("Hello world");

        CharacterSubStats subStats = CharacterSubStats.fromSubStatBuilder(mockSubStatBuilder);

        DuplicateKeyException duplicateStatException = assertThrows(DuplicateKeyException.class,
                () -> principalStatTest.addNewSubStatWithNoDuplicationNameAndSizeLimit(subStats));

        assertEquals("SubStat can't be in the list two time", duplicateStatException.getMessage());
    }

    @Test
    void testCreatePrincipalStat_whenSubStatListIsProvided_aPrincipalStatShouldHas4SubStatWithSameType() {
        CharacterPrincipalStat principalStatTest = CharacterPrincipalStat.fromBuilder(createTestPrincipalStat());

        for (int i = 0; i < 3; i++) {
            CharacterSubStatsBuilder mockSubStatBuilder = mock(CharacterSubStatsBuilder.class);
            when(mockSubStatBuilder.getStatType()).thenReturn(StatType.DEXTERITY);
            when(mockSubStatBuilder.getSubStatName()).thenReturn("Erudition");
            when(mockSubStatBuilder.getSubStatValue()).thenReturn(30);
            when(mockSubStatBuilder.getDescription()).thenReturn("Hello world");

            CharacterSubStats subStats = CharacterSubStats.fromSubStatBuilder(mockSubStatBuilder);
            subStatsList.add(subStats);
        }

        IllegalArgumentException subStatNumberException = assertThrows(IllegalArgumentException.class,
                () -> principalStatTest.verifyPrincipalStatWithSpecificTypeHas4SubStatWithThisType(principalStatTest));

        assertEquals("Principal stat should have four sub Stat with the same type", subStatNumberException.getMessage());
    }

    @Test
    void testCreatePrincipalStat_whenSubStatListIsComplete_principalStatShouldHave20ItemInSubStatList() {
        CharacterPrincipalStat principalStatTest = CharacterPrincipalStat.fromBuilder(createTestPrincipalStat());

        for (StatType statType : StatType.values()) {
            for (int i = 0; i < 4; i++) {
                CharacterSubStatsBuilder mockSubStatBuilder = mock(CharacterSubStatsBuilder.class);
                when(mockSubStatBuilder.getStatType()).thenReturn(statType);
                when(mockSubStatBuilder.getSubStatName()).thenReturn("Erudition" + i);
                when(mockSubStatBuilder.getSubStatValue()).thenReturn(30);
                when(mockSubStatBuilder.getDescription()).thenReturn("Hello world");

                CharacterSubStats subStats = CharacterSubStats.fromSubStatBuilder(mockSubStatBuilder);
                subStatsList.add(subStats);
            }
        }
        principalStatTest.completeListOfSubStatForAllPrincipalStat();
        assertEquals(20, principalStatTest.getSubStatsList().size());
    }

    @Test
    void testCreatePrincipalStat_whenPrincipalStatTypeIsProvided_returnListOfSubStatWithTheSameType() {
        statType = StatType.DEXTERITY;
        IllegalArgumentException subStatTypeException = assertThrows(IllegalArgumentException.class,
                () -> CharacterPrincipalStat.fromBuilder(createTestPrincipalStat()));

        assertEquals("All sub Stat should be have same type than principal Stat", subStatTypeException.getMessage());
    }

    @Test
    void shouldExportCharacterPrincipalStat() {
        CharacterPrincipalStatExport exportPrincipalStat = mock(CharacterPrincipalStatExport.class);
        CharacterPrincipalStat principalStatTest = CharacterPrincipalStat.fromBuilder(createTestPrincipalStat());
        principalStatTest.exportTo(exportPrincipalStat);

        verify(exportPrincipalStat).setType(eq(StatType.PSYCHIC));
        verify(exportPrincipalStat).setValue(eq(45));
        verify(exportPrincipalStat).setListSubStat(eq(subStatsList));
    }

}
