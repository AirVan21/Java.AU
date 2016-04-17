package ru.spbau.functional;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by airvan21 on 21.03.16.
 */
public class Function1Test {

    private final static Function1<Integer, Integer> F = arg -> 2 * arg;
    private final static Function1<Integer, Integer> G = arg -> 3 + arg;
    private final static int CONST_FIVE = 5;

    @Test
    public void testApply() {
        assertEquals(new Integer(CONST_FIVE * 2), F.apply(CONST_FIVE));
        assertEquals(new Integer(CONST_FIVE + 3), G.apply(CONST_FIVE));
    }

    @Test
    public void testApplyOnInheritance() {
        final Function1<BasketballTeam, String> thr  = arg -> "Throw!";
        final Function1<FootballTeam, String>   kick = arg -> "Kick!";
        final Function1<Chelsea, String>        goal = arg -> "Chelsea scores!";

        thr.apply(new BasketballTeam());
        kick.apply(new FootballTeam());
        kick.apply(new Chelsea());
        goal.apply(new Chelsea());

        /* Hadn't typechecked:

        goal.apply(new FootballTeam());
        thr.apply(new FootballTeam());
        goal.apply(new BasketballTeam());
        kick.apply(new BasketballTeam());

        */
    }

    @Test
    public void testCompose() {
        assertEquals(new Integer(CONST_FIVE * 2 + 3), F.compose(G).apply(CONST_FIVE));
        assertEquals(new Integer((CONST_FIVE + 3) * 2), G.compose(F).apply(CONST_FIVE));

        final Function1<FootballTeam, String>       kick = arg -> "Kick!";
        final Function1<FootballTeam, FootballTeam> id   = arg -> arg;
        final Function1<String, String>             goal = arg -> "Goal!";

        assertEquals("Kick!", id.compose(kick).apply(new FootballTeam()));
        assertEquals("Goal!", kick.compose(goal).apply(new Chelsea()));
    }

    private static class BasketballTeam {}
    private static class FootballTeam {}
    private static class Chelsea extends FootballTeam {}
}