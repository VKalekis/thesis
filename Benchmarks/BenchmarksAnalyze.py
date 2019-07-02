#!/usr/bin/python

import csv
import matplotlib.pyplot as plt
import pandas as pd
import numpy as np
from scipy import stats
from matplotlib.font_manager import FontProperties
import os
from scipy.interpolate import make_interp_spline, BSpline

my_path = 'C:/Users/Bill/Documents/Φυσικό/Πτυχιακή/Πτυχιακή-Latex/results'

fontP = FontProperties()
fontP.set_size('small')

# files = ['init_benchmarks6.csv']
# n_label = 'n'
# time_labels = ['time']
# labels = ['TestFunction6']
# colors = ['black']
# filenames = ['/Initial3.pdf', '/Initial4.pdf']


# files = ['init_benchmarks4.csv', 'init_benchmarks5.csv',
#          'init_benchmarks6.csv']
# n_label = 'n'
# time_labels = ['time', 'time', 'time', 'time', 'time']
# labels = ['TestFunction1', 'TestFunction2', 'TestFunction3', 'TestFunction4', 'TestFunction5']
# colors = ['black', 'red', 'blue', 'green', 'purple']
# filenames = ['/Initial33.pdf', '/Initial44.pdf']


#
# files = ['init_benchmarks1.csv', 'init_benchmarks2.csv', 'init_benchmarks3.csv', 'init_benchmarks4.csv',
#          'init_benchmarks5.csv']
# n_label = 'n'
# time_labels = ['time', 'time', 'time', 'time', 'time']
# labels = ['TestFunction1', 'TestFunction2', 'TestFunction3', 'TestFunction4', 'TestFunction5']
# colors = ['black', 'red', 'blue', 'green', 'purple']
# filenames = ['/Initial1.pdf', '/Initial2.pdf']

# files = ['diffequation_JavaD.csv', 'diffequation_CD.csv', 'diffequation_JavaF.csv', 'diffequation_CF.csv']
# n_label = 'Number of points'
# time_labels = ['Java', 'C', 'Java', 'C']
# labels = ['Java Double', 'C Double', 'Java Float', 'C Float']
# colors = ['black', 'red','blue', 'green']
# filenames = ['/DiffEq1.pdf', '/DiffEq2.pdf']

files = ['hash_JavaMD5.csv', 'hash_CMD5.csv', 'hash_JavaSHA-1.csv', 'hash_CSHA-1.csv', 'hash_JavaSHA-256.csv',
'hash_CSHA-256.csv']
n_label = 'String size'
time_labels = ['MD5', 'MD5', 'SHA-1', 'SHA-1', 'SHA-256', 'SHA-256']
labels = ['Java MD5', 'C MD5',  'Java SHA-1',  'C SHA-1', 'Java SHA-256','C SHA-256']
colors = ['black', 'red', 'blue', 'green', 'purple', 'yellow']
filenames = ['/HashFunctions1.pdf','/HashFunctions2.pdf']

# files = ['hash_JavaSHA-256.csv', 'hash_CSHA-256.csv']
# n_label = 'String size'
# time_labels = ['SHA-256', 'SHA-256']
# labels = ['Java SHA-256', 'C SHA-256']
# colors = ['black', 'red']
# filenames = ['/HashFunctionsSHA2561.pdf','/HashFunctionsSHA2562.pdf']

# files = ['quicksort_JavaNoPass.csv', 'quicksort_CNoPass.csv']
# n_label = 'Array Elements'
# time_labels = ['Java', 'C']
# labels = ['Java NoPass', 'C NoPass']
# colors = ['black', 'red']
# filenames = ['/qsortNoPass1.pdf','/qsortNoPass2.pdf']


# files = ['quicksort_JavaPass.csv', 'quicksort_CPass.csv','quicksort_CPassO0.csv']
# n_label = 'Array Elements'
# time_labels = ['Java', 'C', 'C']
# labels = ['Java Pass (O2)', 'C Pass (O2)','C Pass (O0)']
# colors = ['black', 'red', 'blue']
# filenames = ['/qsortPass1.pdf','/qsortPass2.pdf']



# files = ['RBTree_Java.csv', 'RBTree_C.csv','RBTree_Java70k.csv', 'RBTree_C70k.csv']
# n_label = 'Nodes'
# time_labels = ['Java', 'C','Java', 'C']
# labels = ['RBTree Java (200000)', 'RBTree C (200000)','RBTree Java (70000)', 'RBTree C (70000)']
# colors = ['black', 'red', 'blue', 'green']
# filenames = ['/rbtree1.pdf','/rbtree2.pdf']

# files = ['RBTreeClasses_Java.csv', 'RBTreeClasses_C.csv']
# n_label = 'Nodes'
# time_labels = ['Java', 'C']
# labels = ['RBTree Java', 'RBTree C']
# colors = ['black', 'red']
# filenames = ['/rbtreeclasses1.pdf', '/rbtreeclasses2.pdf']

X = []
STD = []

for index in range(0, len(files)):

    df = pd.read_csv('C:/Users/Bill/Desktop/BenchmarksFinal/' + files[index])
    df[time_labels[index]] = (df[time_labels[index]].astype(float)).div(np.float_power(10, 6))
    df = df.drop(df.columns[df.columns.str.contains('unnamed', case=False)], axis=1)

    X1 = []
    STD1 = []
    n = [2 ** i for i in range(1, 17)]
    for i in range(1, 17):
        x = (df.loc[df[n_label] == 2 ** i]).iloc[:, 1]

        x = 1000/1024*2**i / x


        print(2 ** i)

        X1.append(x.mean())

        STD1.append(1.96 * x.std() / np.sqrt(1000))

        # print('JavaMean ' + str(x.mean()))
        # print('CMean ' + str(y.mean()))
        # print('JavaSTD ' + str(x.std()))
        # print('JavaError ' + str(x.std() / np.sqrt(1000)))
        # print('CSTD' + str(1.96*y.std()))
        # print('CError' + str(1.96*y.std() / np.sqrt(1000)))

        # print(x['Java'])

    X.append(X1)
    STD.append(STD1)
print(len(X))
print(X)
print(STD1)
fig = plt.figure(figsize=(8, 6))
ax = fig.add_subplot(111)
plt.grid(None, 'major', 'both')
plt.xlabel('$n=2^i$')
plt.ylabel('$time (ms)$')
#plt.ylabel('$Data-rate (KB/s)$')

for index in range(0, len(X)):
    X1 = X[index]
    STD1 = STD[index]
    slope, intercept, r_value, p_value, std_err = stats.linregress(n[0:8], X1[0:8])
    x = [0, 260]
    y = [slope * x1 + intercept for x1 in x]
    ax.plot(x, y, '--', color=colors[index], linewidth=0.8)


    ax.errorbar(n[0:8], X1[0:8], yerr=STD1[0:8], label=labels[index], fmt='o', markersize=2, capsize=2,
                color=colors[index])
leg = plt.legend(loc='lower right')
# plt.show()

plt.savefig(my_path + filenames[0])

fig2 = plt.figure(figsize=(8, 6))
ax2 = fig2.add_subplot(111)
plt.grid(None, 'major', 'both')
plt.xlabel('$n=2^i$')
plt.ylabel('$time (ms)$')
#plt.ylabel('$Data-rate (KB/s)$')

for index in range(0, len(X)):
    X1 = X[index]
    STD1 = STD[index]
    slope, intercept, r_value, p_value, std_err = stats.linregress(n[8:], X1[8:])
    x = [510, 70000]
    y = [slope * x1 + intercept for x1 in x]

    # x=np.linspace(510,70000,50)
    # spl = make_interp_spline(n[8:], X1[8:], k=3)  # BSpline object
    # power_smooth = spl(x)
    # ax2.plot(x, power_smooth,'--', color=colors[index], linewidth=0.8)


    ax2.plot(x, y, '--', color=colors[index], linewidth=0.8)
    ax2.errorbar(n[8:], X1[8:], yerr=STD1[8:], label=labels[index], fmt='o', markersize=2, capsize=2,
                 color=colors[index])
leg = plt.legend(loc='lower right')
# plt.show()

plt.savefig(my_path + filenames[1])


with open('LatexArray.txt', 'w') as file:
    np.set_printoptions(precision=5)
    for j in range(0, len(n)):
        file.write(str(n[j]) + ' & ')
        for i in range(0,3):
            file.write(str(round(X[i][j], 5)) + ' & ' + str(round(STD[i][j], 5)))
            if i != len(X) - 1:
                file.write(' & ')
        file.write(str("\\") + '\n')
