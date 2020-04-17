package com.akivaGrobman.Game.Client.Backend.Players;

import com.akivaGrobman.Game.Client.Backend.GameObjects.Pieces.PieceColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    private Player player;

    @BeforeEach
    void setUp() {
        player = new Player(PieceColor.WHITE);
    }

    @Test
    void WilNotSendIncompleteMove() {
        Error thrown = assertThrows(
            Error.class,
                () -> player.getMove(),
                "Move data is not complete and ready for usage"
        );

        assertTrue(thrown.getMessage().contains("Move data is not complete and ready for usage"));
    }

}