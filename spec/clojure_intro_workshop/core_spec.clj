(ns clojure-intro-workshop.core-spec
  (:require [speclj.core :refer :all]
            [clojure-intro-workshop.core :refer :all]))


(describe "world-width"
  (it "returns the amount of elements in one line"
    (should= 2
             (world-width [[0 0]
                           [0 0]
                           [0 0]]))))

(describe "world-height"
  (it "returns the amount of lines"
    (should= 3
             (world-height [[0 0]
                            [0 0]
                            [0 0]]))))

(describe "element-at"
  (with world [[0 0]
               [1 0]
               [0 0]])
  (it "returns the correct element value"
    (should= 1
             (element-at @world [0 1]))
    (should= 0
             (element-at @world [0 0])))
  (it "returns 0 for elements that are out of bound"
    (should= 0
             (element-at @world [-1 0]))))


(describe "update-world"
  (with blinker [[0 1 0]
                 [0 1 0]
                 [0 1 0]])

  (it "implements the blinker correctly"
    (should= [[0 0 0]
              [1 1 1]
              [0 0 0]]
             (update-world @blinker))))
