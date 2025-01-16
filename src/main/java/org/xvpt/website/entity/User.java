package org.xvpt.website.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@Document
@AllArgsConstructor
@NoArgsConstructor
public class User extends TrackingEntity {
    @Id
    private String id;
    private String bio;
    private String email;

    private String firstname;
    private String lastname;

    @DBRef
    private Media avatar;
    private List<String> roles;
}
