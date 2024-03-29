(ns solve-my-sudoku.core
  (:gen-class)
  (:refer-clojure :exclude [==]) ;; core.logic defines :== as well
  (:require [clojure.string :as s]
            [clojure.core.logic :refer :all]
            [clojure.core.logic.fd :as fd]))


;;
;;  [* * * * * * * * *
;;   . . . . . . . . .
;;   . . . . . . . . .
;;   . . . . . . . . .
;;   . . . . . . . . .
;;   . . . . . . . . .
;;   . . . . . . . . .
;;   . . . . . . . . .
;;   . . . . . . . . .]
;;
(def row-indexes (partition 9 (range 81)))

;;
;;  [* . . . . . . . .
;;   * . . . . . . . .
;;   * . . . . . . . .
;;   * . . . . . . . .
;;   * . . . . . . . .
;;   * . . . . . . . .
;;   * . . . . . . . .
;;   * . . . . . . . .
;;   * . . . . . . . .]
;;
(def column-indexes (apply map vector row-indexes))

;;
;;  [* * * . . . . . .
;;   * * * . . . . . .
;;   * * * . . . . . .
;;   . . . . . . . . .
;;   . . . . . . . . .
;;   . . . . . . . . .
;;   . . . . . . . . .
;;   . . . . . . . . .
;;   . . . . . . . . .]
;;
(def square-indexes [[0  1  2  9  10 11 18 19 20]
                     [3  4  5  12 13 14 21 22 23]
                     [6  7  8  15 16 17 24 25 26]
                     [27 28 29 36 37 38 45 46 47]
                     [30 31 32 39 40 41 48 49 50]
                     [33 34 35 42 43 44 51 52 53]
                     [54 55 56 63 64 65 72 73 74]
                     [57 58 59 66 67 68 75 76 77]
                     [60 61 62 69 70 71 78 79 80]])



(defn indexed-sub-board
  "Return a board split into sub-boards by given index set.

  (indexed-sub-board BOARD row-indexes)

  Would create all 9 rows"
  [b index-set]
  (letfn [(sub-board [n] (vals (select-keys b (nth index-set n))))]
    (doall (map sub-board (range 9)))))

(defn solve
  "Takes a sudoku as a vector, with all empty cells set to 0 and returns
  a solved sudoku as a vector.

  (solve
    [0 0 0 2 6 0 7 0 1
     6 8 0 0 7 0 0 9 0
     1 9 0 0 0 4 5 0 0
     8 2 0 1 0 0 0 4 0
     0 0 4 6 0 2 9 0 0
     0 5 0 0 0 3 0 2 8
     0 0 9 3 0 0 0 7 4
     0 4 0 0 5 0 0 3 6
     7 0 3 0 1 8 0 0 0])
  "
  [sudoku]
  (let [board (vec (map #(if (zero? %) (lvar) %) sudoku))
        rows (indexed-sub-board board row-indexes)
        cols (indexed-sub-board board column-indexes)
        squares (indexed-sub-board board square-indexes)]

    (first
     (run 1 [q]
       (== q board)
       ;; only 1 - 9 are allowed
       (everyg #(fd/in % (fd/interval 1 9)) board)

       ;; every number can appear only once per row
       (everyg fd/distinct rows)
       ;; every number can appear only once per column
       (everyg fd/distinct cols)
       ;; every number can appear only once per square
       (everyg fd/distinct squares)))))

;; https://dingo.sbs.arizona.edu/~sandiway/sudoku/examples.html
(def wildcatjan17
  [0 0 0 2 6 0 7 0 1
   6 8 0 0 7 0 0 9 0
   1 9 0 0 0 4 5 0 0
   8 2 0 1 0 0 0 4 0
   0 0 4 6 0 2 9 0 0
   0 5 0 0 0 3 0 2 8
   0 0 9 3 0 0 0 7 4
   0 4 0 0 5 0 0 3 6
   7 0 3 0 1 8 0 0 0])

(defn print-sudoku
  [sudoku]
  (doall (map #(println (s/join " " %)) (partition 9 sudoku))))

(defn -main
  "Hello world."
  [& args]
  (println "Input sudoku board (0 represent unfilled)")
  (print-sudoku wildcatjan17)
  (println "solved")
  (print-sudoku (solve wildcatjan17)))
