<template>
  <div id="app">
    <img src="./assets/logo.png">
    <div class="#show">动态内容: {{ this.Moment.content}}</div>
    <div class="#show">发布者: {{ this.User.name }}</div>
    <button @click="ok()" >{{ this.msg }}</button>
    <ul>
      <div v-for="item in commentList">
        {{ item.User.name + ': ' + item.Comment.content }}
      </div>
    </ul>
  </div>
</template>
<script>
  import axios from 'axios'
  // axios.defaults.withCredentials = true

  let url_base = "http://apijson.cn:8080"
  let url_get = url_base + "/get"
  let url_head = url_base + "/head"
  let url_post_get = url_base + "/post_get"
  let url_post_head = url_base + "/post_head"
  let url_post = url_base + "/post"
  let url_put = url_base + "/put"
  let url_delete = url_base + "/delete"

  let model = {
    msg: 'APIJSON',
    Moment: {
      content: 'content'
    },
    User: {
      name: 'name'
    },
    commentList: {
    }
  }

  export default {
    name: 'app',
    data () {
      return model
    },
    methods: {
      ok() {
        // 刷新界面
        function refresh(data) {
          data = data || {}
          model.Moment = data.Moment || {}
          model.User = data.User || {}
          model.commentList = data['[]'] || []

          console.log('refresh  model = \n' + JSON.stringify(model))
        }

        axios.post(url_get,
            {
              'Moment': {
                'userId': 93793
              },
              'User': {
                'id': 93793
              },
              '[]': {
                'count': 5,
                'Comment': {
                  'momentId@': 'Moment/id'
                },
                'User': {
                  'id@': '/Comment/userId'
                }
              }
            })
          .then(function (response) {
            refresh(response.data)
          })
          .catch(function (error) {
            console.log(error);
          })

      }
    }
  }
</script>

<style>
  #app {
    font-family: 'Avenir', Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    text-align: center;
    color: #2c3e50;
    margin-top: 60px;
  }

  h1, h2 {
    font-weight: normal;
  }

  ul {
    list-style-type: none;
    padding: 0;
  }

  li {
    display: inline-block;
    margin: 0 10px;
  }

  a {
    color: #42b983;
  }


  #show {
    height: 100px;
    width: 1000px;
    background-color: #000000;
    text-decoration-color: #ffffff;
  }

</style>
