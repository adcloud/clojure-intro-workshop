;; managing state

;; agents
;; --------------

(def score (agent 0))
(deref score) ;; 0
@score ;; 0
;; an agent is in charge of managing the state
;; agents guarantee that changes happen in the order they arrive

(send score inc) ;; #<Agent@6d8f3cc5: 0>
;; send is async
;; one has to send a function to the agent
;; which will be applied by the agent with the state as a argument
@score ;; 1

(send score + 100)
;; the first argument to the update function is always the state
;; an agent is managing, but additional arguments to the function can be
;; supplied.

(send-off some-agent some-io-bound-fn)
;; Clojure uses a thead pool of cpu-count + 1 for send calls
;; When using send-off Clojure uses a separate thread,
;; having a lot of i/o-bound threads in one pool can block the pool unnecessarily.
;; so use send-off for i/o-bound functions
