print("\n---------------------------------- BEGIN ---------------------------------------\n")


from sklearn.cluster import SpectralClustering
import numpy as np
import pandas as pd
from sklearn import preprocessing

df = pd.read_csv("../Data.csv", usecols=[1,3,4,5,6,11,15], engine="python", delimiter=";")



X = df[['MW17','MW89','MW90','MW91','MW92','MW97','MW101']].as_matrix().tolist()
X_train = []
enc = preprocessing.LabelEncoder()
for column in X:
	if len(set(column)) <= 1:
		print(column)
		df = df.drop('column_name', 1)
	enc.fit(column)
	X_train.append(enc.transform(column))

print("\n-------------------------------------------------------------------------------\n")


clustering = SpectralClustering(n_clusters=2,
            assign_labels="discretize",eigen_solver='amg',
            random_state=0,
            n_jobs = 2
            ).fit(np.array(X_train))

print(clustering.labels_)
print("\n-------------------------------------------------------------------------------\n")
print(clustering )
