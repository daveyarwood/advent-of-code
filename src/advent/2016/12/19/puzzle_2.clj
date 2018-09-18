(ns advent.2016.12.19.puzzle-2
  (:require [advent.2016.12.19.puzzle-1 :as p1]))

(comment
  "First, I adjusted p1/turn based on the new victim selection rules:")

(defn turn
  [{:keys [elves thief] :as state}]
  (if (<= (count elves) 1)
    state
    (let [thief-index        (.indexOf elves thief)
          distance-to-victim (quot (count elves) 2)
          victim-index       (-> thief-index
                                 (+ distance-to-victim)
                                 (rem (count elves)))
          next-elves         (concat (take victim-index elves)
                                     (drop (inc victim-index) elves))
          thief-index        (.indexOf next-elves thief)
          next-thief         (nth next-elves (-> thief-index
                                                 inc
                                                 (rem (count next-elves))))]
      {:elves next-elves
       :thief next-thief})))

(comment
  "Then, as in puzzle 1, I did some testing:"

  (with-redefs [p1/turn turn]
    (doseq [n (map inc (range 500))]
      (prn :n n :winner (p1/winner n)))))

(comment
  "There was still a clear pattern, albeit a much weirder one that has to do
   with both the indexes (starting at 1) and the numbers generated thus far in
   the sequence:"

  1 1 3 1 2 3 5 7 9 1 2 3 4 5 6 7 8 9 11 13 15 17 19 21 23 25 27 1 2 3 4 ...

  "I would describe the pattern like this:

   i=1, n=1; this doesn't really fit into the pattern, which starts at i=2.

   i=2, n=1. From there, start adding 2 so that we're working with odd
   numbers. We'll start doing something new when i=n.

   i=3, n=1+2=3, so i=n, so we're done with the odd numbers. Now, we will jump
   back to n=1 and start adding 1 so it's all integers. We'll do that until
   i=2n.

   i=4, n=1
   i=5, n=2
   i=6, n=3, so i=2n. Now we'll start adding 2 again so we're working with odd
   numbers. We'll switch again once i=n.

   i=7, n=5
   i=8, n=7
   i=9, n=9, so i=n. Back to n=1 and adding 1 until i=2n.

   etc.

   So I coded up this pattern as a lazy sequence and used that to find the
   answer.")

(defn winner-sequence*
  [i interval base]
  (let [n               (+ base interval)
        switch?         (case interval
                          1 (= i (* 2 n))
                          2 (= i n))
        [interval base] (if switch?
                          (if (= 1 interval) [2 n] [1 0])
                          [interval (+ base interval)])]
    (lazy-seq (cons n (winner-sequence* (inc i) interval base)))))

(def winner-sequence
  (cons 1 (winner-sequence* 2 2 -1)))

(defn -main
  []
  (-> winner-sequence
      (nth (dec p1/number-of-elves))
      prn))

