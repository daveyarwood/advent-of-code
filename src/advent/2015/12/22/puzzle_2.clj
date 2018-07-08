(ns advent.2015.12.22.puzzle-2
  (:require [advent.2015.12.22.puzzle-1 :as p1]))

(defn lose-1-hp
  [state]
  (or (p1/battle-over state)
      (let [[player boss mana-spent] state]
        [(update player :hp dec) boss mana-spent])))

(defn player-turn
  [state selected-spell]
  (-> state
      lose-1-hp
      p1/apply-effects-pre
      (p1/cast-spell selected-spell)
      (p1/apply-effects-post selected-spell)))

(defn -main
  []
  (with-redefs [p1/player-turn player-turn]
    (-> [p1/player p1/boss 0] p1/cheapest-victory-in-fewest-moves prn)))

