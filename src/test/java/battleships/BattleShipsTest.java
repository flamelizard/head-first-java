package battleships;

import com.headfirst.battleships.Board;
import com.headfirst.battleships.Ship;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Random;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by Tom on 4/19/2016.
 */
public class BattleShipsTest {

    @Test(expected = IllegalArgumentException.class)
    public void invalidBoardSize() {
        Board board = new Board(-1);
    }

    @Test
    public void boardMethods() {
        Board board = new Board();
        board.makeShips(3);
        assertThat(board.getShips(), hasSize(3));

        board.makeShips(1);
        assertThat("discard old and make new ships",
                board.getShips(), hasSize(1));

        Ship firstShip = board.getShips().get(0);
        ArrayList<String> firstLoc = new ArrayList<>(firstShip.getLocation());
//        foor loop does not work well if its iterable is updated during run
        for (String loc: firstLoc) {
            board.playerTurn(loc);
        }
        assertThat("ship is down", firstShip.isDown(), is(true));
        assertThat("player turns counter",
                board.getNumPlayerTurns(), is(firstLoc.size()));

    }

    @Test
    public void Ships() {
        int shipCount = 10;
        Board board = new Board(5);
        board.makeShips(shipCount);

        int randomShipNum = new Random().nextInt(shipCount);
        Ship ship = board.getShips().get(randomShipNum);
        ArrayList<String> location = new ArrayList<>(ship.getLocation());

        assertThat("hit ship", ship.isHit(location.get(0)), is(true));
        assertThat("miss ship", ship.isHit(location.get(0)), is(false));

        if (location.size() > 1) {
            assertThat("hit ship",
                    ship.isHit(location.get(location.size() - 1)), is(true));
            assertThat("miss ship",
                    ship.isHit(location.get(location.size() - 1)), is(false));
        }

        for (String loc: location) {
            ship.isHit(loc);
        }
        assertThat("ship down", ship.isDown(), is(true));
    }
}
