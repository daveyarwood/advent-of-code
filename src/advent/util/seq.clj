(ns advent.util.seq)

(defn rotate
  [amount coll]
  (let [amount (if (neg? amount)
                 (- (count coll)
                    (- (rem amount (count coll))))
                 amount)]
    (->> coll cycle (drop amount) (take (count coll)))))

