import pandas
import numpy as np
from sklearn import tree
import subprocess
from sklearn.tree import  export_graphviz

"""
each branche get from decision tree model
represent automate pattern
"""

# read data
df = pandas.read_csv('labeled.csv', engine="python",delimiter=r";")
#df.drop(df.index[:10], inplace=False)

X_train = df[['MW17','MW89','MW90','MW91','MW92','MW97','MW101']].as_matrix().tolist()
Y_train = df['label'].tolist()

# create decision tree
dt = tree.DecisionTreeClassifier()
dt = dt.fit(X_train, Y_train)

# show tree rules on file
with open("pattern.txt", "w") as f:
    f = tree.export_graphviz(dt, out_file=f)
