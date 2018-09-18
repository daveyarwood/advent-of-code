(ns advent.2016.12.19.puzzle-1)

(def number-of-elves 3004953)

(comment
  "I first did some testing on low numbers using the following implementation of
   a simulation of the gift exchange:")

(defn initial-state
  [number-of-elves]
  {:elves (map inc (range number-of-elves))
   :thief 1})

(defn turn
  [{:keys [elves thief] :as state}]
  (if (<= (count elves) 1)
    state
    (let [thief-index  (.indexOf elves thief)
          victim-index (-> thief-index inc (rem (count elves)))
          next-elves   (concat (take victim-index elves)
                               (drop (inc victim-index) elves))
          thief-index  (.indexOf next-elves thief)
          next-thief   (nth next-elves (-> thief-index
                                           inc
                                           (rem (count next-elves))))]
      {:elves next-elves
       :thief next-thief})))

(defn winner*
  [{:keys [elves thief]}]
  (when (= 1 (count elves))
    (first elves)))

(defn winner
  [number-of-elves]
  (->> number-of-elves
       initial-state
       (iterate turn)
       (filter winner*)
       (drop-while (complement identity))
       first
       :elves
       first))

(comment
  "Then I examined the results of `(winner number-of-elves)` for all numbers 1
   through 100, and I saw an obvious pattern.")

(map (comp winner inc) (range 100))
;; 1 1 3 1 3 5 7 1 3 5 7 9 11 13 15...

(comment
  "I was then able to simplify my solution and compute the result much faster:")

(def odd-numbers
  (iterate #(+ % 2) 1))

(defn winner-sequence*
  [n]
  (lazy-seq (concat (take n odd-numbers)
                    (winner-sequence* (* n 2)))))

(def winner-sequence
  (winner-sequence* 1))

(defn -main
  []
  (-> winner-sequence
      (nth (dec number-of-elves))
      prn))

