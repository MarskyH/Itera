package com.example.itera.dto.nonFunctionalRequirement;



import com.example.itera.domain.nonFunctionalRequirement.NonFunctionalRequirement;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public record NonFunctionalRequirementResponseDTO(String id, String title, String description, JsonNode weights, Boolean multiple) {
    public NonFunctionalRequirementResponseDTO(NonFunctionalRequirement nonFunctionalRequirement) {
        this(
                nonFunctionalRequirement.getId(),
                nonFunctionalRequirement.getTitle(),
                nonFunctionalRequirement.getDescription(),
                convertWeightsToNode(nonFunctionalRequirement.getWeights()), // Convertendo a string JSON para JsonNode
                nonFunctionalRequirement.getMultiple()
        );
    }

    // Método para converter a string JSON em JsonNode
    private static JsonNode convertWeightsToNode(String weights) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readTree(weights);
        } catch (JsonProcessingException e) {
            // Trate a exceção de acordo com a necessidade
            e.printStackTrace();
            return null;
        }
    }
}

