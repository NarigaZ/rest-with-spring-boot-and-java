package br.com.narigaz.restwithspringbootandjava.data.vo.v1;

import java.io.Serializable;
import java.util.Objects;

public class PersonVO implements Serializable {
    private long id;
    private String firstName;
    private String lastName;
    private String address;
    private String gender;

    public PersonVO() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonVO person = (PersonVO) o;
        return id == person.id && firstName.equals(person.firstName) && lastName.equals(person.lastName) && address.equals(person.address) && gender.equals(person.gender);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, address, gender);
    }
}
