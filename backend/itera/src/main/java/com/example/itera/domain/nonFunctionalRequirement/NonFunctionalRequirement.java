package com.example.itera.domain.nonFunctionalRequirement;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.*;


import java.io.IOException;

@Entity
@Table(name = "nonfunctionalrequirement")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class NonFunctionalRequirement {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "title", length = 100, nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "weights", columnDefinition = "jsonb", nullable = false)
    private String weights;

    @Column(name = "multiple", nullable = false)
    private Boolean multiple;



    // Getter and Setter for weights as JsonNode
    @JsonIgnore
    public JsonNode getWeightsJson() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(weights);
    }

    @JsonIgnore
    public void setWeightsJson(JsonNode weightsJson) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        this.weights = objectMapper.writeValueAsString(weightsJson);
    }
}

