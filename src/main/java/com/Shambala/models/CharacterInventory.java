package com.Shambala.models;

import com.Shambala.models.builder.CharacterInventoryBuilder;
import com.Shambala.models.export.CharacterInventoryExport;
import lombok.Getter;

import java.util.List;

@Getter
public class CharacterInventory {

    private List<CharacterEquipment> characterEquipment;
    private List<String> bag;
    private int goldAmount;


    public static CharacterInventory fromInventoryBuilder(CharacterInventoryBuilder characterInventoryBuilder) {
        CharacterInventory characterInventory = new CharacterInventory();
        characterInventory.characterEquipment = characterInventoryBuilder.getCharacterEquipment();
        characterInventory.bag = characterInventoryBuilder.getBag();
        characterInventory.goldAmount = characterInventoryBuilder.getGoldAmount();
        return characterInventory;
    }

    public void exportInventory(CharacterInventoryExport inventoryExport) {
        inventoryExport.setCharacterEquipment(characterEquipment);
        inventoryExport.setBag(bag);
        inventoryExport.setGoldAmount(goldAmount);
    }
}
