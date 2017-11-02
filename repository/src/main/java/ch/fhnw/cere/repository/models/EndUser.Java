package ch.fhnw.cere.repository.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Entity
public class EndUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private long applicationId;

    private String username;

    private Date created_at;

    private Date updated_at;

    private int phone_nr;




}