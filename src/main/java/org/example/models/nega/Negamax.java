package org.example.models.nega;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import org.example.models.Direction;
import org.example.models.Matrix;

public class Negamax {

  private static final int MAX_DEPTH = 4;

  private static int cnt;

  private Negamax() {
  }

  public static State makeDecision(State state) {
    cnt = 0;
    return state.getActions().stream()
        .max(Comparator.comparing(Negamax::perform)).get();
  }

  private static int perform(State state) {
    cnt++;
    if (state.isTerminal()) {
      return state.getMetrics();
    }
    return state.getActions().stream()
        .map(Negamax::perform)
        .max(Comparator.comparing(Integer::valueOf)).get();
  }

  /*

      private static int maxValue(State state) {
        cnt++;
        if (state.isTerminal()) {
          return state.getMetrics();
        }
        return state.getActions().stream()
            .map(Negamax::minValue)
            .max(Comparator.comparing(Integer::valueOf)).get();
      }

      private static int minValue(State state) {
        cnt++;
        if (state.isTerminal()) {
          return state.getMetrics();
        }
        return state.getActions().stream()
            .map(Negamax::maxValue)
            .min(Comparator.comparing(Integer::valueOf)).get();
      }

      private static int expectMaxValue(State state, boolean player) {
        if (state.isTerminal()) {
          return state.getMetrics();
        }
        if (cnt % 2 == 1) {
          int expected = 0;
          for (State st : state.getActions()) {
            int value = expectMaxValue(st, !player);
            expected = expected + value;
          }
        } else if(player){
          return state.getActions().stream()
              .map(Negamax::maxValue)
              .min(Comparator.comparing(Integer::valueOf)).get();
        }
        return state.getActions().stream()
            .map(Negamax::minValue)
            .min(Comparator.comparing(Integer::valueOf)).get();

      }

    */
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
        return state.getMetrics();
      }
    }

}
