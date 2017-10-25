# Snake

A simple snake game demo in cljs for react native.
The idea is to show advantage and differences in using cljs:
- Figwheel hot load code
- Immutable and persistent data structure
- A glimpse of clojure syntatic extension / macro
- etc

Based on [Re-natal](https://github.com/drapanjanas/re-natal).
For more complete documentation on how to run re-natal based project, check the link.

Step to run this app on ios device simulator:

1.
```
npm i -g re-natal
re-natal use-ios-device simulator
re-natal use-figwheel
lein figwheel ios
```


2.
```
npm install -g react-native-cli
react-native run-ios
```
