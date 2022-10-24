package org.example.models.nega;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import org.example.models.Direction;
import org.example.models.Matrix;

public class NegamaxAlphaBeta {

  private static final int MAX_DEPTH = 6;

  private static int cnt;

  private NegamaxAlphaBeta() {
  }

  public static State makeDecision(State state, int alpha, int beta) {
    cnt = 0;
    State theBestChild = state.getActions().stream()
        .max(Comparator.comparing(NegamaxAlphaBeta::perform)).get();
    if (state.getMetrics() > theBestChild.getMetrics()) {
      return state;
    }
    return theBestChild;
  }

  private static int perform(State state) {
    cnt++;
    if (state.isTerminal()) {
      return state.getMetrics();
    }
    return state.getActions().stream()
        .map(NegamaxAlphaBeta::perform)
        .max(Comparator.comparing(Integer::valueOf)).get();
  }

  public record State(Matrix state, int color) {

    Collection<State> getActions() {
      List<State> actions = new ArrayList<>();
      if (state.playerCanMove(Direction.UP)) {
        Matrix moved = state.playerMove(Direction.UP);
        actions.add(new State(moved, -color));
      }
      if (state.playerCanMove(Direction.DOWN)) {
        Matrix moved = state.playerMove(Direction.DOWN);
        actions.add(new State(moved, -color));
      }
      if (state.playerCanMove(Direction.LEFT)) {
        Matrix moved = state.playerMove(Direction.LEFT);
        actions.add(new State(moved, -color));
      }
      if (state.playerCanMove(Direction.RIGHT)) {
        Matrix moved = state.playerMove(Direction.RIGHT);
        actions.add(new State(moved, -color));
      }
      return actions;
    }

    boolean isTerminal() {
      return cnt > MAX_DEPTH || state.isFinish();
    }

    int getMetrics() {
      return color * state.getMetrics();
    }
  }

}
