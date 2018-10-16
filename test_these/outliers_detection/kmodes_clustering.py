import numpy as np
import pandas
from kmodes.kmodes import KModes
from matplotlib import pyplot


# random categorical data
df = pandas.read_csv('../Data.csv', usecols=[1,3,4,5,6,11,15], engine="python",delimiter=r";")
df.drop(df.index[:10], inplace=False)
data = df[['MW17','MW89','MW90','MW91','MW92','MW97','MW101']].as_matrix().tolist()

km = KModes(n_clusters=20, init='Cao', n_init=10,max_iter=400, verbose=1)

clusters = km.fit_predict(data)

# Print the cluster centroids
print(km.cluster_centroids_)
