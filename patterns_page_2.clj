;; managing state

;; references
;; --------------
(def player-name (ref "Itchy"))
;; #'user/player-name

player-name
; #<Ref@54088e9b: "Itchy">

(deref player-name) ;; "Itchy"
@player-name        ;; "Itchy"

;; references can only be changed inside a
;; software transaction
(dosync
  (ref-set player-name "Scratchy")) ;; "Scratchy"

@player-name ;; "Scratchy"

;; agents
;; --------------

(def score (agent 0))
@score ;; 0
(deref score) ;; 0
;; a agent is in charge of managing the state
;; agents garantee that changes happen in the order they arrive

(send score inc) ;; #<Agent@6d8f3cc5: 0>
@score ;; 0
;; send is async
;; one has to send a function to the agent
;; which will be applyed by the agent with the state as a argument
@score ;; 1

(send score + 100)
;; the first argument to the update function is always the state
;; an agent is managing, but additional arguments to the function can be
;; supplyed.

(send-off some-agent some-io-bound-fn)
;; Clojure uses a thead pool of cpu-count + 1 for send calls
;; When using send-off Clojure uses a seperate thread
;; having a lot of ip bound threads in one pool can block the pool unnessessarily
;; so use send-off for io bound functions
