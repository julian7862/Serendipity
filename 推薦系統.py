from flask import Flask, request
from flask import render_template
import numpy as np
import pandas as pd
import torch
import re
import csv
import os
import sys
from sklearn.metrics.pairwise import paired_distances,cosine_similarity
from fast_bert.learner_cls import BertLearner
from fast_bert.metrics import accuracy
from fast_bert.data_cls import BertDataBunch
import logging
import simplejson as json
import pymysql


app = Flask(__name__)

def get_the_most_similar_movies(user_id, user_movie_matrix,num):
        """找尋與特定user距離最近/最相似的前幾餐廳"""
        user_vec = user_movie_matrix.loc[user_id].values 
        sorted_index = np.argsort(user_vec)[:num]
        return list(user_movie_matrix.columns[sorted_index])
 
def get_the_most_similar_users(movie_id, user_movie_matrix,num):
        """找尋與特定餐廳距離最近/最相似的前幾餐廳"""
        movie_vec = user_movie_matrix[movie_id].values 
        sorted_index = np.argsort(movie_vec)[:num]
        return list(user_movie_matrix.index[sorted_index])    

def edu_dis(arr1,arr2):
        dis = np.zeros((arr1.shape[0],arr2.shape[0]))
        length = len(arr2)
        for i in range(length):
            fun = lambda x:get_euclidean(x, arr2[i])
            dis[:,i] = np.apply_along_axis(fun,1,arr1)
        return dis

def get_euclidean(arr1, arr2):
        #point array type
        return np.sqrt(sum(pow(arr1 - arr2, 2)))

@app.route('/',methods=["GET","POST"])

def index():

    db_setting={
        "host":"127.0.0.1",
        "port":3306,
        "user":"root",
        "password":"julian789",
        "db":"final",
        "charset":"utf8"
    
    }
    #function
    """
    def get_the_most_similar_movies(user_id, user_movie_matrix,num):
        
        user_vec = user_movie_matrix.loc[user_id].values 
        sorted_index = np.argsort(user_vec)[:num]
        return list(user_movie_matrix.columns[sorted_index])
 
    def get_the_most_similar_users(movie_id, user_movie_matrix,num):
        
        movie_vec = user_movie_matrix[movie_id].values 
        sorted_index = np.argsort(movie_vec)[:num]
        return list(user_movie_matrix.index[sorted_index])    

    def edu_dis(arr1,arr2):
        dis = np.zeros((arr1.shape[0],arr2.shape[0]))
        length = len(arr2)
        for i in range(length):
            fun = lambda x:get_euclidean(x, arr2[i])
            dis[:,i] = np.apply_along_axis(fun,1,arr1)
        return dis

    def get_euclidean(arr1, arr2):
        #point array type
        return np.sqrt(sum(pow(arr1 - arr2, 2)))
    """
    #df1 = pd.read_csv(r'C:/Users/mypc\Downloads/restaurant.csv', encoding='utf-8',header=None)
    #print(df1)
    learner=torch.load(r'C:/Users/mypc\Downloads/new/model.pt')
    texts1 = request.args.get("name")
    #texts2 = ['我想吃火鍋']
    myList = list()
    myList.append(texts1)
    predictions1 = learner.predict_batch(myList)
    matrix1 = np.reshape(predictions1,(33,2))
    predictions1=pd.DataFrame(matrix1)
    predictions3=predictions1.iloc[0:5,0]
    print(predictions3)
    user=pd.read_csv(r'C:/Users/mypc\Downloads/user1.csv', encoding='utf-8')
    #print(user)
    try:
        conn=pymysql.connect(**db_setting)

        with conn.cursor() as cursor:
        
            command = "SELECT 編號,種類一,種類二,種類三,種類四,( 6371 * acos ( cos ( radians(22.626543) ) * cos( radians( lat ) ) * cos( radians( lng ) - radians(120.265869) ) + sin ( radians(22.626543) ) * sin( radians( lat ) ) ) )AS distance FROM node HAVING distance < 2 ORDER BY distance"

            cursor.execute(command)
            result=cursor.fetchall()
            #print(type(result))
            df = pd.DataFrame(result,columns=["編號","種類一","種類二","種類三","種類四","距離"])
        #print(df)
    except Exception as ex:
        print(ex)
    c=''
    for f in range(0,5):
        if(f<4):
            b = str(predictions3.loc[f])+"|"
            c=c+b
        else:
            b= str(predictions3.loc[f])
            c=c+b
    user.loc[0,"genres"]=c
   
    dummies = user["genres"].str.get_dummies('|')
    user_vec = pd.concat([user, dummies], axis=1)
    user_vec.drop(['genres'],axis=1,inplace=True)
    user_vec = user_vec.groupby("userId").mean()
#print(user_vec)
#print(user_vec.shape)

    df1=df[["編號","種類一","種類二","種類三","種類四"]]
#print(df1)

    x=[]
    for i in range(0, len(df1)):
        for j in range(1,5):
            x.append(str(df.iloc[i,j]))
#print(x)

    df0=df1['編號']

    pd2= pd.DataFrame(np.repeat(df0.values,4),columns=["0"])
#print(pd2)
    pd2['label']=x
#print(pd2)
    z=pd.DataFrame(columns=["0"])
    for j in range(0,33):
        z.loc[j]="test"

#pdd = pd2.append(z,ignore_index=True)

#print(pdd)

    y = ["下午茶",	"中式料理",	"冰品飲料",	"午餐",	"合菜",	"咖哩",	"咖啡",	"宵夜",	"寵物友善"	,"小吃"	,"居酒屋","拉麵",	"日本料理",	
              "早午餐",	"早餐","晚餐"	,"景觀餐廳",	"泰式料理"	,"港式料理",	"火鍋",	"燒烤",	"牛排",	"牛肉麵",	"甜點",	"精緻高級"	,"約會餐廳"	
              ,"素食"	,"美式料理","義式料理","蛋糕"	,"親子餐廳",	"韓式料理",	"餐酒館/酒吧"] 
#y2=pd.DataFrame(y,columns=["label"])     
    z.insert(1,'label',y)
    pdd2 = pd2.append(z,ignore_index=True)
#print(pdd2)
    pd3 = pd.concat([pdd2, pd.get_dummies(pdd2.label)], 1).groupby(["0"]).sum().reset_index()
    pd3 = pd3.drop(pd3.columns[1], axis=1)
#print(pd3)
#print(pd3.shape)

    pd3.index.names = ["restId"]
#print(pd3)
    
    a=pd3[pd3["0"].isin(["test"])].index.values[0]
    pd3=pd3.drop(index=a,axis=0,inplace=False)
    pd3.reset_index()
#pd3.info()
#pd3.rename(columns={0:"rest"},inplace=True)
    pd4 = pd3.drop(columns=["0"])
   
#pd4.info() 

#print(pd4.shape)
    user_comment_matrix = cosine_similarity(user_vec.values,pd4.values)
#print(user_comment_matrix) 
    user_comment_matrix = 1- user_comment_matrix 
    user_comment_matrix = pd.DataFrame(user_comment_matrix, index=user_vec.index,columns=pd4.index)
#print(user_comment_matrix) 


    r=[]

    r=get_the_most_similar_movies(1,user_comment_matrix,20)
    #print(r)

    user_rest=pd3.loc[r]
    user_rest=user_rest['0']
    h=[]
    for o in range(0,20):
        h.append("編號")
    user_rest.index = h
    print(user_rest)
    print(type(user_rest))
    json_values= user_rest.to_json(force_ascii=False,orient='records')
    return json_values

if __name__ == '__main__':
    app.debug = True
    app.run(host="0.0.0.0",port=5000)   