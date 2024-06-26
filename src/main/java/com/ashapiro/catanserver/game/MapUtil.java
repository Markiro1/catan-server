package com.ashapiro.catanserver.game;

import com.ashapiro.catanserver.game.enums.EdgeBuildingType;
import com.ashapiro.catanserver.game.enums.HarborType;
import com.ashapiro.catanserver.game.enums.VertexBuildingType;
import com.ashapiro.catanserver.game.model.Edge;
import com.ashapiro.catanserver.game.model.User;
import com.ashapiro.catanserver.game.model.Vertex;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MapUtil {

    public static List<Vertex> getAvailableVerticesForUser(User user, List<Edge> edges) {
        return edges.stream()
                .filter(edge -> edge.getUser() != null && edge.getUser().equals(user))
                .flatMap(edge -> edge.getNeighborVertices().stream())
                .filter(vertex -> vertex.getType().equals(VertexBuildingType.NONE))
                .toList();
    }

    public static List<Vertex> getUserVertices(User user, List<Vertex> vertices) {
        return vertices.stream()
                .filter(vertex -> vertex.getUser() != null && vertex.getUser().equals(user))
                .toList();
    }

    public static List<Edge> getAvailableEdgesForUser(User user, List<Vertex> vertices, List<Edge> edges) {
        Set<Edge> availableEdges = vertices.stream()
                .filter(vertex -> vertex.getUser() != null && vertex.getUser().equals(user))
                .flatMap(vertex -> vertex.getNeighbourEdges().stream())
                .filter(edge -> edge.getType().equals(EdgeBuildingType.NONE))
                .collect(Collectors.toSet());

        edges.stream()
                .filter(edge -> edge.getUser() != null && edge.getUser().equals(user))
                .map(edge -> getNeighboursEdgeToEdge(edge))
                .forEach(availableEdges::addAll);

        return new ArrayList<>(availableEdges);
    }

    public static List<Edge> getNeighboursEdgeToEdge(Edge edge) {
        return edge.getNeighborVertices().stream()
                .flatMap(vertex -> vertex.getNeighbourEdges().stream())
                .filter(e -> e.getType().equals(EdgeBuildingType.NONE) && !e.equals(edge))
                .toList();
    }

    public static boolean isUserHaveSpecialHarborWithType(List<Vertex> userVertices, HarborType harborType) {
        return userVertices.stream()
                .anyMatch(vertex -> vertex.getHarborType() != null &&
                        vertex.getHarborType().equals(harborType) &&
                        (vertex.getType().equals(VertexBuildingType.SETTLEMENT) || vertex.getType().equals(VertexBuildingType.CITY)));

    }

    public static boolean isUserHaveGenericHarbor(List<Vertex> userVertices) {
        return userVertices.stream()
                .anyMatch(vertex -> vertex.getHarborType() != null &&
                        vertex.getHarborType().equals(HarborType.GENERIC) &&
                        (vertex.getType().equals(VertexBuildingType.SETTLEMENT) || vertex.getType().equals(VertexBuildingType.CITY)));
    }
}
