<?php

function fix_question_type($key){
  return ucwords(str_replace("_", " ", $key));
}

function not_null($value){
  return !is_null($value);
}