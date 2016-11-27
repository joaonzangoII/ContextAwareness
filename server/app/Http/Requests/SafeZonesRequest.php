<?php

namespace App\Http\Requests;

use Illuminate\Foundation\Http\FormRequest;

class SafeZonesRequest extends FormRequest
{
  /**
   * Determine if the user is authorized to make this request.
   *
   * @return bool
   */
  public function authorize()
  {
    return true;
  }

  /**
   * Get the validation rules that apply to the request.
   *
   * @return array
   */
  public function rules()
  {
    return [
      'name' => 'required',
      'latitude' => 'required|numeric|between:-90.00000,90.00000',
      'longitude' => 'required|numeric|between:-90.00000,90.00000',
      'radius' => 'required|positive',
    ];
  }
}
