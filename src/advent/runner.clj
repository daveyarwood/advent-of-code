(ns advent.runner)

(defn run-solution!
  [{:keys [year day puzzle]}]
  (assert year   ":year required.")
  (assert day    ":day required.")
  (assert puzzle ":puzzle number required.")
  (let [day       (if (< day 10) (str \0 day) (str day))
        puzzle-ns (symbol (format "advent.%s.12.%s.puzzle-%s" year day puzzle))
        main-fn   (symbol (str puzzle-ns "/-main"))]
    (require [puzzle-ns])
    ((resolve main-fn))))
