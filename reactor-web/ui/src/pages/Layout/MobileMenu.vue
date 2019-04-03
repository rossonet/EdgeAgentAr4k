<template>
  <ul class="nav nav-mobile-menu">
    <li>
      <md-field>
        <label>Search</label>
        <md-input v-model="search" type="text"></md-input>
      </md-field>
    </li>
    <li><a href="#" class="dropdown-toggle" data-toggle="dropdown">
      <i class="material-icons">home</i>
      <p>Home</p></a>
    </li>
    <li>
      <drop-down>
        <a slot="title" class="dropdown-toggle" data-toggle="dropdown">
          <i class="material-icons">notifications</i>
          <span class="notification">5</span>
          <p>ToDo</p>
        </a>
        <ul class="dropdown-menu dropdown-menu-right">
          <li><a href="#">Task 1</a></li>
          <li><a href="#">Task 2</a></li>
          <li><a href="#">Task 3</a></li>
          <li><a href="#">Task 4</a></li>
          <li><a href="#">Task 5</a></li>
        </ul>
      </drop-down>
    </li>
    <li>
      <a href="#/user" data-toggle="dropdown" class="dropdown-toggle"><i class="material-icons">person</i>
      <p>Profile</p></a>
    </li>
  </ul>
</template>
<script>
export default {
  data () {
    return {
      search: null,
      selectedEmployee: null,
      employees: [
        'Andrea',
        'Admin',
        'Guest'
      ]
    }
  }
}
</script>
