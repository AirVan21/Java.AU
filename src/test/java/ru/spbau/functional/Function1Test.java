package ru.spbau.functional;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by airvan21 on 21.03.16.
 */
public class Function1Test {

    private final Function1<Integer, Integer> f = arg -> 2 * arg;
    private final Function1<Integer, Integer> g = arg -> 3 + arg;
    private final int CONST_FIVE = 5;

    @Test
    public void testApply() {
        assertEquals(new Integer(CONST_FIVE * 2), f.apply(CONST_FIVE));
        assertEquals(new Integer(CONST_FIVE + 3), g.apply(CONST_FIVE));
    }

    @Test
    public void testApplyOnInheritance() {
        Function1<BasketballTeam, String> thr  = arg -> "Throw!";
        Function1<FootballTeam, String>   kick = arg -> "Kick!";
        Function1<Chelsea, String>        goal = arg -> "Chelsea scores!";

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
        assertEquals(new Integer(CONST_FIVE * 2 + 3), f.compose(g).apply(CONST_FIVE));
        assertEquals(new Integer((CONST_FIVE + 3) * 2), g.compose(f).apply(CONST_FIVE));

        Function1<FootballTeam, String>       kick = arg -> "Kick!";
        Function1<FootballTeam, FootballTeam> id   = arg -> arg;
        Function1<String, String>             goal = arg -> "Goal!";

        assertEquals("Kick!", id.compose(kick).apply(new FootballTeam()));
        assertEquals("Goal!", kick.compose(goal).apply(new Chelsea()));
    }

    private class BasketballTeam {}
    private class FootballTeam {}
    private class Chelsea extends FootballTeam {}
}