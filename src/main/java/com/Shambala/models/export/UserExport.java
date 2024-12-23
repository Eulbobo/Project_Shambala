package com.Shambala.models.export;

import com.Shambala.models.Character;

import java.util.List;

public interface UserExport {
    void setLastName(String lastName);

    void setFirstName(String firstName);

    void setEmail(String email);

    void setNickName(String nickName);

    void setPassword(String password);

    void setListCharacter(List<Character> characterList);
}
