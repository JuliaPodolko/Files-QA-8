package guru.qa.domain;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Teacher {
    public String name;
    public String surname;
    @SerializedName("favorite_subject")
    public List<String> favoriteSubject;
    public Address address;
}
