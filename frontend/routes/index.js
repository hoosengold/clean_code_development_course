let express = require('express');
let router = express.Router();

/* GET home page. */
router.get('/', function(req, res, next) {
  res.render('index');
});
router.get('/login', function(req, res, next) {
  res.render('login_signup');
});
router.get('/signup', function(req, res, next) {
  res.render('login_signup');
});

module.exports = router;
