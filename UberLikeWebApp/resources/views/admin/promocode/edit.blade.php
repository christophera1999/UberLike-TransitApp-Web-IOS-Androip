@extends('admin.layout.base')

@section('title', 'Update Promocode ')

@section('content')

<div class="content-area py-1">
    <div class="container-fluid">
    	<div class="box box-block bg-white">
    	    <a href="{{ route('admin.promocode.index') }}" class="btn btn-default pull-right"><i class="fa fa-angle-left"></i> Back</a>

			<h5 style="margin-bottom: 2em;">Update Promocode</h5>

            <form class="form-horizontal" action="{{route('admin.promocode.update', $promocode->id )}}" method="POST" enctype="multipart/form-data" role="form">
            	{{csrf_field()}}
            	<input type="hidden" name="_method" value="PATCH">
				<div class="form-group row">
					<label for="promo_code" class="col-xs-2 col-form-label">Promocode</label>
					<div class="col-xs-10">
						<input class="form-control" type="text" value="{{ $promocode->promo_code }}" name="promo_code" required id="promo_code" placeholder="Promocode">
					</div>
				</div>

				<div class="form-group row">
					<label for="discount" class="col-xs-2 col-form-label">Discount</label>
					<div class="col-xs-10">
						<input class="form-control" type="number" value="{{ $promocode->discount }}" name="discount" required id="discount" placeholder="Discount">
					</div>
				</div>

				<div class="form-group row">
					<label for="expiration" class="col-xs-2 col-form-label">Expiration</label>
					<div class="col-xs-10">
						<input class="form-control" type="date" value="{{ date('Y-m-d',strtotime($promocode->expiration)) }}" name="expiration" required id="expiration" placeholder="Expiration">
					</div>
				</div>

				<div class="form-group row">
					<label for="zipcode" class="col-xs-2 col-form-label"></label>
					<div class="col-xs-10">
						<button type="submit" class="btn btn-primary">Update Promocode</button>
						<a href="{{route('admin.promocode.index')}}" class="btn btn-default">Cancel</a>
					</div>
				</div>
			</form>
		</div>
    </div>
</div>

@endsection
