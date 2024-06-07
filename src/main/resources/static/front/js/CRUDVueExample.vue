<template>
  <div id="app">
    <h1>Vue.js with Axios CRUD Example</h1>
    <form @submit.prevent="submitForm">
      <div>
        <label for="name">Name:</label>
        <input type="text" v-model="form.name" id="name">
      </div>
      <div>
        <label for="email">Email:</label>
        <input type="email" v-model="form.email" id="email">
      </div>
      <button type="submit">Create User</button>
    </form>

    <div v-if="users.length">
      <h2>User List</h2>
      <ul>
        <li v-for="user in users" :key="user.id">
          {{ user.name }} - {{ user.email }}
          <button @click="editUser(user)">Edit</button>
          <button @click="deleteUser(user.id)">Delete</button>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      form: {
        id: null,
        name: '',
        email: ''
      },
      users: []
    };
  },
  created() {
    this.fetchUsers();
  },
  methods: {
    async fetchUsers() {
      try {
        const response = await axios.get('http://localhost:8080/api/users');
        this.users = response.data;
      } catch (error) {
        console.error('Error fetching users:', error);
      }
    },
    async submitForm() {
      if (this.form.id) {
        // Update user
        try {
          const response = await axios.put(`http://localhost:8080/api/users/${this.form.id}`, this.form);
          this.fetchUsers();
          this.resetForm();
        } catch (error) {
          console.error('Error updating user:', error);
        }
      } else {
        // Create user
        try {
          const response = await axios.post('http://localhost:8080/api/users', this.form);
          this.users.push(response.data);
          this.resetForm();
        } catch (error) {
          console.error('Error creating user:', error);
        }
      }
    },
    async deleteUser(id) {
      try {
        await axios.delete(`http://localhost:8080/api/users/${id}`);
        this.users = this.users.filter(user => user.id !== id);
      } catch (error) {
        console.error('Error deleting user:', error);
      }
    },
    editUser(user) {
      this.form = { ...user };
    },
    resetForm() {
      this.form = { id: null, name: '', email: '' };
    }
  }
};
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  text-align: center;
  margin-top: 60px;
}
form {
  max-width: 300px;
  margin: 0 auto;
}
div {
  margin-bottom: 10px;
}
label {
  display: block;
  margin-bottom: 5px;
}
input {
  width: 100%;
  padding: 8px;
  box-sizing: border-box;
}
button {
  padding: 10px 20px;
}
</style>