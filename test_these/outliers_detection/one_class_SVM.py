import pandas
import numpy as np
import matplotlib.pyplot as plt
import matplotlib.font_manager
from sklearn import svm
from sklearn.model_selection import train_test_split
from collections import Counter

## one class SVM 
"""
algorithm for classify automate states to {normal, anormal}
"""
# read data
df = pandas.read_csv('../Data.csv', usecols=[1,3,4,5,6,11,15], engine="python",delimiter=r";")
df.drop(df.index[:10], inplace=False)

# split data
X_train, X_test = train_test_split(df, test_size=0.2)

# fit the model
clf = svm.OneClassSVM(nu=0.1, kernel="poly", gamma=0.1)
clf.fit(X_train)
y_pred_train = clf.predict(X_train)
y_pred_test = clf.predict(X_test)

# print result
print("Normal data"+str(y_pred_train.count(1)))
print(Counter(y_pred_test))
df['label'] = pandas.Series(list(y_pred_train) + list(y_pred_test))
df.to_csv("labeled.csv", sep=';')

# plot

print("--------------------------------------------------------------------------")
## kmodes clustering
data = df[['MW17','MW89','MW90','MW91','MW92','MW97','MW101']].as_matrix().tolist()

# clusterize data
km = KModes(n_clusters=20, init='Cao', n_init=10,max_iter=400, verbose=1)
clusters = km.fit_predict(data)



