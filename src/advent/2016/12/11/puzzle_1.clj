(ns advent.2016.12.11.puzzle-1
  (:require [clojure.java.io            :as io]
            [clojure.math.combinatorics :as combo]
            [clojure.set                :as set]
            [clojure.string             :as str]))

(comment
  "I spun my wheels on this one for weeks before finally giving up. I was able
   to get to where I could return a decision tree of all of the possible
   solutions, but then I couldn't wrap my head around turning that decision tree
   into a flat list of the number of steps in each solution.")

(defn parse-object
  [s]
  (->> s
       (re-matches #"a ([a-z]+)(?:-compatible)? (generator|microchip)")
       rest
       (map keyword)))

(defn parse-floor
  [s]
  (as-> s ?
    (re-matches #"The [a-z]+ floor contains (.*)\." ?)
    (last ?)
    (str/split ? #",? and |, ")
    (filter #(not= "nothing relevant" %) ?)
    (map parse-object ?)
    (set ?)))

(defn initial-state
  [floors]
  {:floors floors, :elevator-pos 0})

(defn possible-moves
  [{:keys [floors elevator-pos]}]
  (let [possible-directions   (case elevator-pos
                                0 [:up]
                                3 [:down]
                                [:up :down])
        objects-on-this-floor (nth floors elevator-pos)
        possible-payloads     (->> objects-on-this-floor
                                   seq
                                   combo/subsets
                                   (filter #(<= 1 (count %) 2))
                                   (map set))]
    (for [direction possible-directions
          payload possible-payloads]
      {:direction direction :payload payload})))

(defn safe?
  [{:keys [floors]}]
  (every? (fn [objects]
            (let [{microchips :microchip, generators :generator}
                  (group-by second objects)]
              (or (empty? generators)
                  (every? (fn [[m-element _]]
                            (some (fn [[g-element _]]
                                    (= m-element g-element))
                                  generators))
                          microchips))))
          floors))

(defn apply-move
  [{:keys [direction payload]} {:keys [floors elevator-pos]}]
  (let [new-elevator-pos (case direction
                           :up   (inc elevator-pos)
                           :down (dec elevator-pos))]
    {:elevator-pos new-elevator-pos
     :floors       (for [[i objects] (map-indexed list floors)]
                     (cond
                       ;; remove objects from the floor we're leaving
                       (= elevator-pos i)
                       (set/difference objects payload)

                       ;; add objects to the floor we're entering
                       (= new-elevator-pos i)
                       (set/union objects payload)

                       ;; the other floors remain unchanged
                       :else
                       objects))}))

(defn direction-is
  [dir]
  (fn [{:keys [direction]}]
    (= dir direction)))

(defn payload-size-is
  [n]
  (fn [{:keys [payload]}]
    (= n (count payload))))

(defn optimization-1
  "When all the floors below a given floor are empty, don't bother moving
   downward."
  [{:keys [floors elevator-pos]} moves]
  (if (->> floors (take elevator-pos) (every? empty?))
    (remove (direction-is :down) moves)
    moves))

(defn optimization-2
  "When moving upstairs, if two objects can be brought upstairs, don't bother
   bringing one object upstairs.

   Similarly, when moving downstairs, if one object can be brought upstairs,
   don't bother bringing two objects downstairs."
  [_ moves]
  (let [upward-moves   (filter (direction-is :up) moves)
        downward-moves (filter (direction-is :down) moves)]
    (concat (if (some (payload-size-is 2) upward-moves)
              (filter (payload-size-is 2) upward-moves)
              upward-moves)
            (if (some (payload-size-is 1) downward-moves)
              (filter (payload-size-is 1) downward-moves)
              downward-moves))))

(defn optimize-safe-moves
  [state moves]
  (->> moves
       (optimization-1 state)
       (optimization-2 state)))

(defn safe-moves
  [state]
  (->> state
       possible-moves
       (filter #(safe? (apply-move % state)))
       (optimize-safe-moves state)
       (map #(apply-move % state))))

(defn all-objects-on-top-floor?
  [{:keys [floors]}]
  (->> floors butlast (every? empty?)))

(defn summary-of
  "When finding solutions, we cache previously known states so that if we
   encounter them again, we can avoid duplicating work by going any further.

   As an optimization, when comparing a state against each previous state in our
   cache, we consider the two states equivalent if the relationships between all
   of the microchips and generators are the same, even if some of the pairs of
   elements are swapped.

   For example, if in one state, chip A and generator A are on floors 1 and 2
   respectively, and chip B and generator B are on floors 2 and 1, and in the
   other state, chip A and generator A are on floors 2 and 1 and chip B and
   generator B are or floors 1 and 2, then the two states have the same set of
   relationships between chips and generators, therefore we can save time by
   considering them equivalent.

   The return value of this function is a tuple [elevator-pos relationships].

   `relationships` is a sorted list of integer pairs, each of which is a tuple
   [chip-position generator-position], where each of those is the floor that the
   object is on."
  [{:keys [floors elevator-pos]}]
  [elevator-pos
   (->> floors
        (map-indexed (fn [i objects]
                       (map #(vector i %) objects)))
        (apply concat)
        (group-by (fn [[floor [element object-type]]]
                    element))
        (map (fn [[element objects]]
               (let [{:keys [microchip generator]}
                     (->> objects
                          (group-by (fn [[floor [element object-type]]]
                                      object-type))
                          (map (fn [[object-type [[floor]]]]
                                 [object-type floor]))
                          (into {}))]
                 [microchip generator])))
        sort)])

(defn solutions
  "Given an initial state, returns a tree structure of the possible solutions
   from here. Each branch of the tree is, itself, a tree of possible solutions
   from that state."
  ([state]
   (solutions state #{}))
  ([state previous-state-summaries]
   (let [state-summary (summary-of state)]
     (cond
       ;; If all objects are on the top floor, then this state is a solution.
       (all-objects-on-top-floor? state)
       [state]

       ;; If we've arrived at a state like this before, then we conclude that
       ;; there is no solution to be found from here, and we assume that one of
       ;; the other timelines will arrive upon a solution.
       ;;
       ;; This prevents infinite recursion by cutting off timelines where we
       ;; move back and forth between two equivalent states forever.
       (contains? previous-state-summaries state-summary)
       []

       :else
       (let [state-summaries (conj previous-state-summaries state-summary)]
         (for [next-state (safe-moves state)
               :let [next-steps (solutions next-state state-summaries)]
               :when (seq next-steps)]
           (cons :x #_state next-steps)))))))

(defn solutions-with-less-than-n-steps
  "Given a solutions tree like what `solutions` returns, returns all solutions
   with less than `n` steps."
  [n solutions-tree])
  ;; ???

(defn -main
  []
  (with-open [rdr (io/reader (io/resource "2016-12-11-01-input"))]
    (->> rdr
         line-seq
         (map parse-floor)
         initial-state
         solutions
         #_(solutions-with-less-than-n-steps 100)
         #_pprint)))

