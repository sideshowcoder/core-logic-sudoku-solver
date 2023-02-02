(defproject solve-my-sudoku "0.1.0-SNAPSHOT"
  :description "Solve sudoku games"
  :url "https://github.com/sideshowcoder/solve-my-sudoku"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [org.clojure/core.logic "0.8.11"]]
  :main ^:skip-aot solve-my-sudoku.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
