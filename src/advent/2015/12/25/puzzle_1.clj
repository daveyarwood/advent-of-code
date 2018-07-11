(ns advent.2015.12.25.puzzle-1)

(def first-code 20151125)

(defn next-code
  [code]
  (-> code (* 252533) (rem 33554393)))

(def codes
  (iterate next-code first-code))

(defn row-indices
  [row]
  (let [col-1-index (-> (reductions + 1 (range)) (nth row))]
    (->> (iterate inc (inc row))
         (reductions + col-1-index))))

(defn code-at-coordinates
  [row col]
  (let [index (-> (row-indices row) (nth (dec col)))]
    (nth codes (dec index))))

(defn -main
  []
  (prn (code-at-coordinates 2947 3029)))
